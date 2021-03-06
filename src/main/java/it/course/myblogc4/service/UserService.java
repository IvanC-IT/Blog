package it.course.myblogc4.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.course.myblogc4.entity.Authority;
import it.course.myblogc4.entity.AuthorityName;
import it.course.myblogc4.entity.User;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.AuthorityRepository;
import it.course.myblogc4.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired UserRepository userRepository;
	@Autowired PasswordEncoder passwordEncoder;
	@Autowired AuthorityRepository authorityRepository;
	
	
	public boolean compareTwoUser(ResponseEntityHandler response, User user1, User user2) {
		if(user1 == user2) {
			response.setMsg("Can't add reaction to your own comment");
			response.setStatus(HttpStatus.BAD_REQUEST);
			return true;
		}
		
		return false;
	}
	
	public User getAuthenticatedUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		List<String> auts = authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
		if(!auts.contains("ROLE_ANONYMOUS")) {
			User u = userRepository.findByUsername(authentication.getName()).get();
			return u;
		}else {
			return null;
		}
		
	}
	
	public void setAuthority(long countUsers, User user) {
		Optional<Authority> userAuthority = Optional.empty();

		if (countUsers > 0) {
			userAuthority = authorityRepository.findByName(AuthorityName.ROLE_READER);
		} else {
			userAuthority = authorityRepository.findByName(AuthorityName.ROLE_ADMIN);
		}

		user.setAuthorities(Collections.singleton(userAuthority.get()));
	}
	
	
	public byte[] getSHA(String input) throws NoSuchAlgorithmException {	
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		return md.digest(input.getBytes(StandardCharsets.UTF_8));
	} 
	
	
	public String toHexString(byte[] hash) {
		
		BigInteger number = new BigInteger(1, hash);
		StringBuilder hexString = new StringBuilder(number.toString(16));
		
		while (hexString.length() < 32) {
			hexString.insert(0, '0');
		}
		
		return hexString.toString().toUpperCase();
		
	}
	
	public Date adjustDate(Date date) {
		LocalDateTime localDateTime = date.toInstant()
			      .atZone(ZoneId.systemDefault())
			      .toLocalDateTime();
		
		Date correctDate = Date.from(localDateTime
			      .atZone(ZoneOffset.UTC)
			      .toInstant());
		
		return correctDate;
	}

}
