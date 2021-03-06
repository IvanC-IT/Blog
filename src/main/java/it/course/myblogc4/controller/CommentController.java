package it.course.myblogc4.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc4.entity.Comment;
import it.course.myblogc4.entity.Post;
import it.course.myblogc4.entity.User;
import it.course.myblogc4.payload.request.CommentRequest;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.CommentRepository;
import it.course.myblogc4.repository.PostRepository;
import it.course.myblogc4.service.UserService;

@RestController
@Validated
public class CommentController {
	
	@Autowired CommentRepository commentRepository;
	@Autowired PostRepository postRepository;
	@Autowired UserService userService;
	
	@PostMapping("private/add-comment/{id}")
	@PreAuthorize("hasRole('READER') or hasRole('EDITOR')")
	public ResponseEntity<ApiResponseCustom> addComment(@PathVariable @NotNull long id,
			@RequestBody @Valid CommentRequest commentRequest, HttpServletRequest request) {
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);

		Optional<Post> p = postRepository.findById(id);
		if (!p.isPresent()) {
			response.setMsg("Post is not present");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		User u = userService.getAuthenticatedUser();
		
		Comment c = new Comment(commentRequest.getComment(), u, p.get());

		
		if(commentRequest.getRefererTo() > 0) {
			Optional<Comment> refererTo = commentRepository.findById(commentRequest.getRefererTo());
			if (!refererTo.isPresent() || id != refererTo.get().getPost().getId()) {
				response.setMsg("Comment (father) is not present");
				response.setStatus(HttpStatus.NOT_FOUND);
				return response.getResponseEntity();
			}	
			
			c.getReferersTo().add(refererTo.get());
		}
		
		commentRepository.save(c);
		
		response.setMsg("New comment added to post: "+id);
		response.setStatus(HttpStatus.CREATED);
		return response.getResponseEntity();
		
	}

}
