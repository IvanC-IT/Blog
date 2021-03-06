package it.course.myblogc4.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.course.myblogc4.entity.Authority;
import it.course.myblogc4.entity.AuthorityName;
import it.course.myblogc4.entity.DbFile;
import it.course.myblogc4.entity.LogoutTrace;
import it.course.myblogc4.entity.User;
import it.course.myblogc4.payload.request.SignInRequest;
import it.course.myblogc4.payload.request.SignUpRequest;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.payload.response.UserResponse;
import it.course.myblogc4.repository.AuthorityRepository;
import it.course.myblogc4.repository.DbFileRepository;
import it.course.myblogc4.repository.LogoutTraceRepository;
import it.course.myblogc4.repository.UserRepository;
import it.course.myblogc4.security.JwtAuthenticationResponse;
import it.course.myblogc4.security.JwtTokenUtil;
import it.course.myblogc4.service.DbFileService;
import it.course.myblogc4.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired 
	DbFileService dbFileService;

	@Autowired 
	DbFileRepository dbFileRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AuthorityRepository authorityRepository;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	UserService userService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	LogoutTraceRepository logoutTraceRepository;

	@Value("${jwt.header}")
	private String tokenHeader;
	
	@PostMapping("private/logout")
	public ResponseEntity<ApiResponseCustom> logout(HttpServletRequest request) {
	
		ResponseEntityHandler response;
		
		String token = request.getHeader(tokenHeader);
		Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);
		
		/*LocalDateTime localDateTime = expiration.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDateTime();
		
		Date expirationToken = Date.from(localDateTime
			      .atZone(ZoneOffset.UTC)
			      .toInstant());*/
		Date expirationToken = userService.adjustDate(expiration);
		
		logoutTraceRepository.save(new LogoutTrace(token,expirationToken));
		
		response = new ResponseEntityHandler(request);
		response.setMsg("Logout ok");
		return response.getResponseEntity();
	}
	@PostMapping("public/signup")
	@Transactional
	public ResponseEntity<ApiResponseCustom> signUp(HttpServletRequest request,
			@Valid @RequestBody SignUpRequest signUpRequest) {

		long countUsers = userRepository.count(); // select count(*) from user;

		Object msg;
		HttpStatus status;
		ResponseEntityHandler response;

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			msg = "User already registered";
			status = HttpStatus.OK;

		} else if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			msg = "Username already in use";
			status = HttpStatus.OK;

		} else {
			User user = new User(signUpRequest.getEmail(), signUpRequest.getUsername(), signUpRequest.getPassword());
			user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
			userRepository.save(user);
			userService.setAuthority(countUsers, user);

			msg = "User successfully registered. Please Sign In now";
			status = HttpStatus.OK;
		}

		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();
	}

	@PostMapping("public/signin")
	public ResponseEntity<ApiResponseCustom> signIn(@Valid @RequestBody SignInRequest signInRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, JsonProcessingException {

		Optional<User> user = userRepository.findByUsernameOrEmail(signInRequest.getUsernameOrEmail(),
				signInRequest.getUsernameOrEmail());

		Object msg;
		HttpStatus status;
		ResponseEntityHandler responseEntity;

		if (!user.isPresent()) {
			msg = "Please register yourself before the Sign In";
			status = HttpStatus.BAD_REQUEST;
		}	
		else {
			if(user.get().getBannedUntil() != null) {
				Date todayDate = new Date();
				if (todayDate.after(user.get().getBannedUntil())) {
					user.get().setEnabled(true);
					user.get().setBannedUntil(null);
					userRepository.save(user.get());
				} else {
					responseEntity = new ResponseEntityHandler("The user is banned until " + user.get().getBannedUntil(), request, HttpStatus.FORBIDDEN);
					return responseEntity.getResponseEntity();
				}
			}
			
			int la = user.get().getLoginAttemp();
			
			if(la < 3) {			
			user.get().setLoginAttemp(user.get().getLoginAttemp()+1);
			}
			else {
			user.get().setLoginAttemp(0);
			user.get().setBannedUntil(DateUtils.addMinutes(new Date(), 15));
			}
			userRepository.save(user.get());
		
			
			final Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(user.get().getUsername(), signInRequest.getPassword()));


			SecurityContextHolder.getContext().setAuthentication(authentication);
			final UserDetails userDetails = userDetailsService.loadUserByUsername(user.get().getUsername());
			final String token = jwtTokenUtil.generateToken(userDetails);
			response.setHeader(tokenHeader, token);
			user.get().setLoginAttemp(0);
			userRepository.save(user.get());
			
			msg = new JwtAuthenticationResponse(userDetails.getUsername(), userDetails.getAuthorities(), token);
			status = HttpStatus.OK;
		}

		responseEntity = new ResponseEntityHandler(msg, request, status);
		return responseEntity.getResponseEntity();
	}

	@GetMapping("private/get-me")
	public ResponseEntity<ApiResponseCustom> getMe(HttpServletRequest request) {

		User user = userService.getAuthenticatedUser();
		Set<AuthorityName> authorities = user.getAuthorities().stream().map(a -> a.getName())
				.collect(Collectors.toSet());
		UserResponse userResponse = new UserResponse(user.getUsername(), user.getEmail(), authorities,user.getAvatar());

		ResponseEntityHandler response = new ResponseEntityHandler(userResponse, request, HttpStatus.OK);
		return response.getResponseEntity();
	}

	@PutMapping("private/add-authority")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> addAuthority(HttpServletRequest request, @RequestParam long id,
			@RequestParam String authority) {

		Optional<User> userOptional = userRepository.findById(id);
		Optional<Authority> authorityOptional = authorityRepository.findByName(AuthorityName.valueOf(authority));

		Object msg;
		HttpStatus status;
		ResponseEntityHandler response;

		if (!userOptional.isPresent()) {
			msg = "User not found";
			status = HttpStatus.NOT_FOUND;

		} else if (!authorityOptional.isPresent()) {
			msg = "Authority not found";
			status = HttpStatus.NOT_FOUND;

		} else {
			User userEntity = userOptional.get();
			Authority authorityEntity = authorityOptional.get();
			userEntity.getAuthorities().add(authorityEntity);
			userRepository.save(userEntity);

			msg = "Authority " + authorityEntity.getName() + " added to user " + userEntity.getUsername();
			status = HttpStatus.OK;
		}

		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();
	}

	@PutMapping("private/remove-authority")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> removeAuthority(HttpServletRequest request, @RequestParam long id,
			@RequestParam String authority) {

		Optional<User> userOptional = userRepository.findById(id);
		Optional<Authority> authorityOptional = authorityRepository.findByName(AuthorityName.valueOf(authority));
		User userEntity = userOptional.get();
		Authority authorityEntity = authorityOptional.get();
		Set<Authority> authorityListFromUser = userOptional.get().getAuthorities();

		Object msg;
		HttpStatus status;
		ResponseEntityHandler response;

		if (!userOptional.isPresent()) {
			msg = "User not found";
			status = HttpStatus.NOT_FOUND;

		} else if (!authorityOptional.isPresent()) {
			msg = "Authority doesn't exist";
			status = HttpStatus.NOT_FOUND;

		} else if (!authorityListFromUser.contains(authorityEntity)) {
			msg = "Authority not found";
			status = HttpStatus.NOT_FOUND;

		} else {
			if (authorityListFromUser.size() > 1) {
				userEntity.getAuthorities().remove(authorityEntity);

				msg = "Authority removed";
				status = HttpStatus.OK;

			} else {
				Authority authorityTmp = authorityRepository.findByName(AuthorityName.valueOf("ROLE_READER")).get();
				Set<Authority> setAuthorityTmp = Collections.singleton(authorityTmp);
				userEntity.setAuthorities(setAuthorityTmp);

				msg = "Removed the last authority. Authority setted to ROLE_READER";
				status = HttpStatus.OK;
			}

			userRepository.save(userEntity);
		}

		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();
	}

	@PutMapping("private/reset-password")
	public ResponseEntity<ApiResponseCustom> resetPassword(HttpServletRequest request, @RequestParam String oldPassword,
			@RequestParam String newPassword, @RequestParam String repeatPassword) {

		User user = userService.getAuthenticatedUser();
		String actualPassword = user.getPassword();

		Object msg;
		HttpStatus status;
		ResponseEntityHandler response;

		if (!passwordEncoder.matches(oldPassword, actualPassword)) {
			msg = "Actual password is wrong";
			status = HttpStatus.BAD_REQUEST;

		} else if (passwordEncoder.matches(newPassword, actualPassword)) {
			msg = "New password must be different from the old password";
			status = HttpStatus.BAD_REQUEST;

		} else if (!newPassword.equals(repeatPassword)) {
			msg = "New password and repeat password must be equals";
			status = HttpStatus.BAD_REQUEST;

		} else {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);

			msg = "New password is saved";
			status = HttpStatus.OK;
		}

		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();
	}

	@Transactional
	@PutMapping("private/add-avatar")
	public ResponseEntity<ApiResponseCustom> addAvatar(HttpServletRequest request, @RequestParam MultipartFile file) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		if(!dbFileService.ctrlAvatarDimension(dbFileService.getBufferedImage(file))) {
			response.setMsg("Avatar image is too large");
			response.setStatus(HttpStatus.BAD_REQUEST);
		}
		if(!dbFileService.ctrlAvatarKb(file)) {
			response.setMsg("Avatar image dimension not allowed");
			response.setStatus(HttpStatus.BAD_REQUEST);
		}
		
		User u = userService.getAuthenticatedUser();
		
		if(dbFileService.ctrlSameImage(file,u.getAvatar())){
			response.setMsg("You cannot set the same image");
			response.setStatus(HttpStatus.BAD_REQUEST);
			return response.getResponseEntity();
		}

		DbFile oldAvatar = u.getAvatar();
		
		try {
			DbFile dbFile = dbFileService.storeDbFile(file);
			u.setAvatar(dbFile);
			if (oldAvatar != null)
				dbFileRepository.delete(oldAvatar);
			
		} catch (IOException e) {
			e.getStackTrace();
		}
		
		response.setMsg("Avatar added");
		return response.getResponseEntity();
	}

}