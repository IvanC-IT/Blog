package it.course.myblogc4.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.course.myblogc4.entity.Authority;
import it.course.myblogc4.entity.AuthorityName;
import it.course.myblogc4.entity.Country;
import it.course.myblogc4.entity.DbFile;
import it.course.myblogc4.entity.Language;
import it.course.myblogc4.entity.MainLand;
import it.course.myblogc4.entity.Post;
import it.course.myblogc4.entity.PostVisited;
import it.course.myblogc4.entity.Tag;
import it.course.myblogc4.entity.User;
import it.course.myblogc4.payload.request.PostCountriesRequest;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.CommentResponse;
import it.course.myblogc4.payload.response.PostCountCommentsResponse;
import it.course.myblogc4.payload.response.PostCountTagResponse;
import it.course.myblogc4.payload.response.PostCountriesResponse;
import it.course.myblogc4.payload.response.PostDetailResponse;
import it.course.myblogc4.payload.response.PostMainLandResponse;
import it.course.myblogc4.payload.response.PostResponse;
import it.course.myblogc4.payload.response.PostSearch;
import it.course.myblogc4.payload.response.PostTagResponse;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.AuthorityRepository;
import it.course.myblogc4.repository.CommentRepository;
import it.course.myblogc4.repository.CountryRepository;
import it.course.myblogc4.repository.DbFileRepository;
import it.course.myblogc4.repository.LanguageRepository;
import it.course.myblogc4.repository.MainLandRepository;
import it.course.myblogc4.repository.PostRepository;
import it.course.myblogc4.repository.PostVisitedRepository;
import it.course.myblogc4.repository.TagRepository;
import it.course.myblogc4.repository.UserRepository;
import it.course.myblogc4.service.DbFileService;
import it.course.myblogc4.service.PostService;
import it.course.myblogc4.service.UserService;


@RestController
@Validated
public class PostController {

	@Autowired PostRepository postRepository;
	@Autowired UserRepository userRepository;
	@Autowired LanguageRepository languageRepository;
	@Autowired UserService userService;
	@Autowired CountryRepository countryRepository;
	@Autowired MainLandRepository mainLandRepository;
	@Autowired TagRepository tagRepository;
	@Autowired CommentRepository commentRepository;
	@Autowired DbFileService dbFileService;
	@Autowired DbFileRepository dbFileRepository;
	@Autowired AuthorityRepository authorityRepository;
	@Autowired PostVisitedRepository postVisitedRepository;
	@Autowired PostService postService;
	@PostMapping("private/add-post")
	@PreAuthorize("hasRole('EDITOR')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> addPost(/*@RequestBody @Valid PostRequest postRequest,*/
			@RequestParam MultipartFile file,
			@NotEmpty @NotBlank @Size(min=1, max=100) String title,
			@NotEmpty @NotBlank @Size(min=1, max=255) String overview,
			@NotEmpty @NotBlank @Size(min=1, max=64000) String content,
			@NotEmpty @NotBlank @Size(min=2, max=2) String langCode,
			HttpServletRequest request) {

		Object msg = null;
		HttpStatus status = HttpStatus.OK;
		ResponseEntityHandler response;
		response = new ResponseEntityHandler(msg, request, status);

		boolean titleExists = postRepository.existsByTitle(title);
		if (titleExists) {
			response.setMsg("Title already present");
			response.setStatus(HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}

		Optional<Language> l = languageRepository.findById(langCode);
		if (!l.isPresent()) {
			response.setMsg("Language not found");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		if(!dbFileService.ctrlPostImageDimension(dbFileService.getBufferedImage(file))) {
			response.setMsg("Post image dimension not allowed");
			response.setStatus(HttpStatus.BAD_REQUEST);
			return response.getResponseEntity();
		}
		
		if(!dbFileService.ctrlPostImageKb(file)) {
			response.setMsg("Post image is too large");
			response.setStatus(HttpStatus.BAD_REQUEST);
			return response.getResponseEntity();
		}
		
		User u = userService.getAuthenticatedUser();
		Post p = new Post(title, overview, content, u, l.get());
		
		try {
			DbFile dbf = dbFileService.storeDbFile(file);
			p.setPostImage(dbf);
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		postRepository.save(p);
		response.setMsg("New Post added");

		return response.getResponseEntity();
	}
/*
	@GetMapping("public/get-post/{id}")
	public ResponseEntity<ApiResponseCustom> getPost(@PathVariable @NotNull long id, HttpServletRequest request) {

		Object msg = null;
		HttpStatus status = HttpStatus.OK;
		ResponseEntityHandler response;

		PostResponse pr = postRepository.getPost(id);

		if (pr == null) {
			msg = "No post found";
			status = HttpStatus.NOT_FOUND;
			response = new ResponseEntityHandler(msg, request, status);
			return response.getResponseEntity();
		}

		msg = pr;
		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();
	}
*/
	@PutMapping("private/update-post/{id}")
	@PreAuthorize("hasRole('EDITOR')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> updatePost(@PathVariable @NotNull long id, 
			@RequestParam MultipartFile file,
			@NotEmpty @NotBlank @Size(min=1, max=100) String title,
			@NotEmpty @NotBlank @Size(min=1, max=255) String overview,
			@NotEmpty @NotBlank @Size(min=1, max=64000) String content,
			@NotEmpty @NotBlank @Size(min=2, max=2) String langCode,
			HttpServletRequest request) {
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);

		User u = userService.getAuthenticatedUser();

		Optional<Post> post = postRepository.findById(id);
		if (!post.isPresent()) {
			response = new ResponseEntityHandler("Post not found", request, HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		Optional<Language> l = languageRepository.findById(langCode);
		if (!l.isPresent()) {
			response = new ResponseEntityHandler("Language not found", request, HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		if (u.getId() != post.get().getAuthor().getId()) {
			response = new ResponseEntityHandler("You cannot update this post", request, HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}

		Optional<Post> duplicateTitle = postRepository.findByTitle(title);

		if (duplicateTitle.isPresent()) {
			if (duplicateTitle.get().getId() != id) {
				response = new ResponseEntityHandler("There is already another post with the same title", request,
						HttpStatus.FORBIDDEN);
				return response.getResponseEntity();
			}
		}
		
		
	
		DbFile oldImage = post.get().getPostImage();
		
		String msgDuplicateImage = "";
		
		if(dbFileService.ctrlSameImage(file, oldImage)) {
			msgDuplicateImage = " You upload the same image.";
		
		} else {
			if(!dbFileService.ctrlPostImageDimension(dbFileService.getBufferedImage(file))) {
				response.setMsg("Post image dimension not allowed");
				response.setStatus(HttpStatus.BAD_REQUEST);
				return response.getResponseEntity();
			}
			
			if(!dbFileService.ctrlPostImageKb(file)) {
				response.setMsg("Post image is too large");
				response.setStatus(HttpStatus.BAD_REQUEST);
				return response.getResponseEntity();
			}
			
			try {
				DbFile dbf = dbFileService.storeDbFile(file);
				post.get().setPostImage(dbf);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(oldImage != null) {
				dbFileRepository.delete(oldImage);
			}
		}

		post.get().setTitle(title);
		post.get().setOverview(overview);
		post.get().setContent(content);
		post.get().setLanguage(l.get());

		post.get().setApproved(false);
		post.get().setPublished(false);

		postRepository.save(post.get());

		response.setMsg("Post updated." + msgDuplicateImage);
		return response.getResponseEntity();
	}



	@PutMapping("private/publish-unpublish-post/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> publishUnpublishPost(@PathVariable @NotNull long id,
			@RequestParam @NotBlank boolean x, HttpServletRequest request) {

		Object msg = null;
		HttpStatus status = HttpStatus.OK;
		ResponseEntityHandler response;

		Optional<Post> post = postRepository.findById(id);
		if (!post.isPresent()) {
			response = new ResponseEntityHandler("Post not found", request, HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		if (x) {
			if (!post.get().getApproved()) {
				response = new ResponseEntityHandler("Approve the post before publish it", request,
						HttpStatus.NOT_FOUND);
				return response.getResponseEntity();
			}
			post.get().setPublished(true);
			msg = "Post " + id + " has been published";
		} else {
			post.get().setPublished(false);
			msg = "Post " + id + " has been unpublished";
		}

		postRepository.save(post.get());

		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();

	}

	@PutMapping("private/approve-post/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> approvePost(@PathVariable @NotNull long id,
			@RequestParam @NotBlank boolean x, HttpServletRequest request) {

		Object msg = null;
		HttpStatus status = HttpStatus.OK;
		ResponseEntityHandler response;

		Optional<Post> post = postRepository.findById(id);
		if (!post.isPresent()) {
			response = new ResponseEntityHandler("Post not found", request, HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		if (!x) {
			post.get().setApproved(false);
			post.get().setPublished(false);
			msg = "Post " + id + " has been disapproved and unpublished";
		} else {
			post.get().setApproved(true);
			msg = "Post " + id + " has been approved";
		}

		postRepository.save(post.get());

		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();

	}

	@PutMapping("private/add-countries-to-post")
	@PreAuthorize("hasRole('EDITOR')")
	public ResponseEntity<ApiResponseCustom> addCountriesToPost(
			@RequestBody @Valid PostCountriesRequest postCountriesRequest, HttpServletRequest request) {

		String msg = "Countries added to post " + postCountriesRequest.getId();
		HttpStatus status = HttpStatus.OK;
		ResponseEntityHandler response;

		Optional<Post> post = postRepository.getPostWithCountries(postCountriesRequest.getId());
		if (!post.isPresent()) {
			response = new ResponseEntityHandler("Post not found", request, HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		if (post.get().getAuthor().getId() != userService.getAuthenticatedUser().getId()) {
			msg = "You are not the owner of this post: " + postCountriesRequest.getId();
			status = HttpStatus.FORBIDDEN;
			response = new ResponseEntityHandler(msg, request, status);
			return response.getResponseEntity();
		}

		Set<Country> cs = countryRepository.findByCountryCodeIn(postCountriesRequest.getCountriesCode());

		// cs.forEach(post.get().getCountries()::add);
		post.get().getCountries().addAll(cs);

		postRepository.save(post.get());

		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();
	}

	@GetMapping("public/get-posts-by-country")
	public ResponseEntity<ApiResponseCustom> getPostByCountry(@RequestParam @NotBlank @NotEmpty String countryCode,
			HttpServletRequest request) {

		HttpStatus status = HttpStatus.OK;
		ResponseEntityHandler response;

		List<Post> ps = postRepository.getPostCountriesResponse(countryCode);

		List<PostCountriesResponse> pcrs = ps.stream()
				.map(p -> new PostCountriesResponse(p.getId(), p.getTitle(), p.getOverview(), p.getUpdatedAt(),
						p.getAuthor().getUsername(), p.getLanguage().getLangName(), (Set<String>) p.getCountries()
								.stream().map(c -> c.getCountryCode()).collect(Collectors.toSet())))
				.collect(Collectors.toList());

		response = new ResponseEntityHandler(pcrs, request, status);
		return response.getResponseEntity();
	}

	@GetMapping("public/get-posts-by-mainland")
	public ResponseEntity<ApiResponseCustom> getPostByMainLand(@RequestParam @NotBlank @NotEmpty String mainLandName,
			HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);
		Optional<MainLand> mainLandOptional = mainLandRepository.findById(mainLandName);

		if (!mainLandOptional.isPresent()) {
			response.setMsg("This mainland doesn't exist in the database");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		List<Post> postList = postRepository.getPostMainLandResponse(mainLandName);

		for (int i = 0; i < postList.size(); i++) {
			Post postTmp = postList.get(i);
			postList.remove(i);

			if (!postList.contains(postTmp))
				postList.add(postTmp);
		}

		List<PostMainLandResponse> postMainLandList = postList.stream()
				.map(p -> new PostMainLandResponse(p.getId(), p.getTitle(), p.getOverview(), p.getUpdatedAt(),
						p.getAuthor().getUsername(), p.getLanguage().getLangName(), p.getCountries().stream()
								.map(c -> c.getMainLand().getMainLandName()).collect(Collectors.toSet())))
				.collect(Collectors.toList());

		response.setMsg(postMainLandList);

		return response.getResponseEntity();
	}

	@PostMapping("private/add-tag-to-post/{id}")
	@PreAuthorize("hasRole('EDITOR')")
	public ResponseEntity<ApiResponseCustom> addTagToPost(@PathVariable @NotNull long id,
			@RequestParam @NotBlank @NotEmpty String tagName, HttpServletRequest request) {

		Object msg = " ";
		HttpStatus status = HttpStatus.OK;
		ResponseEntityHandler response;
		response = new ResponseEntityHandler(msg, request, status);
		
		

		Optional<Post> p = postRepository.findById(id);
		if (!p.isPresent()) {
			response.setMsg("Post is not present");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		User u = userService.getAuthenticatedUser();
		if (u.getId() != p.get().getAuthor().getId()) {
			response.setMsg("You cannot update this post");
			response.setStatus(HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}

		Optional<Tag> t = tagRepository.findById(tagName.toUpperCase());
		if (!t.isPresent()) {
			response.setMsg("Tag is not present");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		if (p.get().getTags().contains(t.get())) {
			response.setMsg("Tag already assigned to post");
			response.setStatus(HttpStatus.BAD_REQUEST);
			return response.getResponseEntity();
		}

		p.get().getTags().add(t.get());
		postRepository.save(p.get());
		response.setMsg("Tag added to post");
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();

	}

	@PutMapping("private/remove-tag-from-post/{id}")
	@PreAuthorize("hasRole('EDITOR')")
	public ResponseEntity<ApiResponseCustom> removeTagFromPost(@PathVariable @NotNull long id,
			@RequestParam @NotBlank @NotEmpty String tagName, HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);

		Optional<Post> p = postRepository.findById(id);
		if (!p.isPresent()) {
			response.setMsg("Post is not present");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		User u = userService.getAuthenticatedUser();
		if (u.getId() != p.get().getAuthor().getId()) {
			response.setMsg("You cannot update this post");
			response.setStatus(HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}

		Optional<Tag> t = tagRepository.findById(tagName.toUpperCase());
		if (!t.isPresent()) {
			response.setMsg("Tag does not exist");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		if (!p.get().getTags().contains(t.get())) {
			response.setMsg("The post does not have that tag");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		p.get().getTags().remove(t.get());
		postRepository.save(p.get());
		response.setMsg("Tag removed");
		response.setStatus(HttpStatus.OK);

		return response.getResponseEntity();

	}
	
	@GetMapping("public/find-posts-by-tag")
	public ResponseEntity<ApiResponseCustom> findPostsByTag(@RequestParam @NotBlank @NotEmpty String tagName, HttpServletRequest request) {
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		Optional<Tag> t = tagRepository.findById(tagName.toUpperCase());
		if (!t.isPresent()) {
			response.setMsg("Tag does not exist");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		List<PostTagResponse> ps = postRepository.getPostsByTag(tagName);
		
		if (ps.isEmpty()) {
			response.setMsg("No posts found with tag: "+tagName);
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		response.setMsg(ps);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();

	}
	
	
	@GetMapping("public/get-post-detail")
	public ResponseEntity<ApiResponseCustom> getPostDetail(@RequestParam @NotNull long postId, HttpServletRequest request,
			@RequestHeader(value = "User-Agent") String userAgent) {
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		String ip = request.getRemoteAddr();
		
		Optional<Post> p = postRepository.findByIdAndPublishedTrue(postId);
		if (!p.isPresent()) {
			response.setMsg("Post is not present");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		User user = userService.getAuthenticatedUser();
		long userId = 0L;

		
		if(user != null) {
			if (user.getAuthorities().contains(new Authority(AuthorityName.ROLE_READER))) {
				
				userId = user.getId();
			}
		}
		PostVisited pv = new PostVisited(p.get(),userId,ip,userAgent);
		postVisitedRepository.save(pv);

		PostDetailResponse pdr = postRepository.getPostDetail(postId);
		List<CommentResponse> cr = commentRepository.getCommentResponse(postId);
		pdr.setComments(cr);
		
		response.setMsg(pdr);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();
	}
	
	@GetMapping("public/get-posts-count-tags")
	public ResponseEntity<ApiResponseCustom> getPostCountTags(HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);
		List<PostCountTagResponse> postList = postRepository.getPostCountTagsResponse();
		
		if(postList.isEmpty()) {
			response.setMsg("There aren't posts from this author");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		response.setMsg(postList);
		
		return response.getResponseEntity();	
	}
	
	@GetMapping("public/get-posts-by-avg")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> getPosts(
			@RequestParam(defaultValue = "ASC") String ordered, // (values = ASC or DESC)
			HttpServletRequest request) {
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		
		// tutti i post
		List<PostResponse> postList = postRepository.getPostsByAvg(ordered);
		
		if(postList.isEmpty()) {
			response.setMsg("No post found");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		response.setMsg(postList);
		return response.getResponseEntity();
		
	}
	
	
	@GetMapping("public/search-sql")
	public ResponseEntity<ApiResponseCustom> searchSql(@RequestParam String keyword,
			@RequestParam boolean isExactMatch,
			@RequestParam boolean isCaseSensitive,
			HttpServletRequest request){
		
		// La ricerca è da effettuare sia nel titolo che nel contenuto
				// booleano
				// keyword = tot
				//exactMatch=true
				//la quantità tot è 70; -> post 1
				// Quantità tot. = 89 -> post 6
				//exactMatch=false
				// la quantità totale è 80; -> post 3
				// la quantità tot è 70; -> post 1 
				// Quantità tot. = 89 -> post 6
				//
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		Instant start = Instant.now();
		
		String keywordExact = "\\b".concat(keyword.concat("\\b"));
		List<Post> ps = new ArrayList<Post>();
		
		if(isCaseSensitive)
			if(isExactMatch)
				ps = postRepository.getPostsVisibleBySearchCaseSensitiveTrue(keywordExact);
			else
				ps = postRepository.getPostsVisibleBySearchCaseSensitiveTrue(keyword);
		else
			if(isExactMatch)
				ps = postRepository.getPostsVisibleBySearchCaseSensitiveFalse(keywordExact);
			else
				ps = postRepository.getPostsVisibleBySearchCaseSensitiveFalse(keyword);
		
		if(ps.isEmpty()) {
			response.setMsg("No post found");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		
		List<PostSearch> prs = ps.stream()
				.map(post-> new PostSearch(
						post.getId(),
						post.getTitle(),
						post.getContent(),
						userRepository.getAuthorUsernameByPostId(post.getId())
						))
				.collect(Collectors.toList());
		
		Instant end = Instant.now();
		System.out.println("Search SQL: "+Duration.between(start, end).toMillis());
		
		
		response.setMsg(prs);
		return response.getResponseEntity();
	}
	
	
	
	@GetMapping("public/search-java")
	public ResponseEntity<ApiResponseCustom> searchJava(@RequestParam String keyword,
			@RequestParam boolean isExactMatch,
			@RequestParam boolean isCaseSensitive,
			HttpServletRequest request){
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		Instant start = Instant.now();
		
		List<PostSearch> ps = postRepository.getPostsVisibleForSearch();
		List<PostSearch> ps2 = new ArrayList<PostSearch>();
		
		if(isExactMatch)
			if(!isCaseSensitive)
				ps2 = ps.stream().filter(p ->
					(postService.isExactMatch(keyword.toLowerCase(), p.getTitle().toLowerCase())) ||
					(postService.isExactMatch(keyword.toLowerCase(), p.getContent().toLowerCase()))	
				).collect(Collectors.toList());
			else
				ps2 = ps.stream().filter(p ->
					(postService.isExactMatch(keyword, p.getTitle())) ||
					(postService.isExactMatch(keyword, p.getContent()))	
				).collect(Collectors.toList());
		else
			if(!isCaseSensitive)
				ps2 = ps.stream().filter(p ->
					p.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
					p.getContent().toLowerCase().contains(keyword.toLowerCase())
				).collect(Collectors.toList());
			else
				ps2 = ps.stream().filter(p ->
					p.getTitle().contains(keyword) ||
					p.getContent().contains(keyword)
				).collect(Collectors.toList());
		
			
		if(ps.isEmpty()) {
			response.setMsg("No post found");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
	
		Instant end  = Instant.now();
		System.out.println("Search JAVA: "+Duration.between(start, end).toMillis());
		
		response.setMsg(ps2);
		return response.getResponseEntity();
	}
	
	@GetMapping("public/search-obj")
	public ResponseEntity<ApiResponseCustom> searchObj(
			@RequestParam String keyword,
			@RequestParam boolean isExactMatch,
			@RequestParam boolean isCaseSensitive, HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);

		Instant start = Instant.now();

		String pattern = (isExactMatch) ? "\\b"+keyword+"\\b" : keyword;
		String caseSensitivity = (isCaseSensitive) ? "c" : "i";

		List<Object> objList = postRepository.getPostByKeyword(pattern,caseSensitivity);

		List<PostSearch> postList = objList.stream()
				.map(ps -> (Object[])ps)
				.map(ps -> new PostSearch(((BigInteger)ps[0]).longValue(),(String)ps[1],(String)ps[2],(String)ps[3])
		).collect(Collectors.toList());

		Instant end = Instant.now();
		System.out.println("Search OBJ: "+ Duration.between(start, end).toMillis());

		if(postList.isEmpty()) {
			response.setMsg("No post found");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		response.setMsg(postList);
		return response.getResponseEntity();

	}
	@GetMapping("public/search-mapping")
	public ResponseEntity<ApiResponseCustom> searchMapping(
			@RequestParam String keyword,
			@RequestParam boolean isExactMatch,
			@RequestParam boolean isCaseSensitive, HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);

		Instant start = Instant.now();

		String pattern = (isExactMatch) ? "\\b"+keyword+"\\b" : keyword;
		String caseSensitivity = (isCaseSensitive) ? "c" : "i";

		List<PostSearch> ps = postRepository.getSearchResponseMapping(pattern,caseSensitivity);

		Instant end = Instant.now();
		System.out.println("Search MAPPING: "+ Duration.between(start, end).toMillis());

		if(ps.isEmpty()) {
			response.setMsg("No post found");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		response.setMsg(ps);
		return response.getResponseEntity();

	}
}
