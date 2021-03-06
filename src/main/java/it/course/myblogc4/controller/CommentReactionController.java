package it.course.myblogc4.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc4.entity.Advisory;
import it.course.myblogc4.entity.AdvisoryStatus;
import it.course.myblogc4.entity.Comment;
import it.course.myblogc4.entity.CommentReaction;
import it.course.myblogc4.entity.CommentReactionId;
import it.course.myblogc4.entity.CommentReactionName;
import it.course.myblogc4.entity.User;
import it.course.myblogc4.payload.request.CommentReactionRequest;

import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.CommentReactionByUser;
import it.course.myblogc4.payload.response.CommentReactionResponse;
import it.course.myblogc4.payload.response.CommentReactionTotalResponse;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.AdvisoryRepository;
import it.course.myblogc4.repository.CommentReactionRepository;
import it.course.myblogc4.repository.CommentRepository;
import it.course.myblogc4.service.CommentReactionService;
import it.course.myblogc4.service.CommentService;
import it.course.myblogc4.service.UserService;

@RestController
public class CommentReactionController {

	@Autowired CommentService commentService;
	@Autowired UserService userService;
	@Autowired CommentRepository commentRepository;
	@Autowired CommentReactionRepository commentReactionRepository;
	@Autowired AdvisoryRepository advisoryRepository;
	@Autowired CommentReactionService commentReactionService;
	
	@PostMapping("private/add-reaction")
	@PreAuthorize("hasRole('READER') or hasRole('EDITOR')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> addReaction(@RequestBody @Valid CommentReactionRequest commentReactionRequest,
			HttpServletRequest request) {

		ResponseEntityHandler response = new ResponseEntityHandler(request);
		Optional<Comment> c = commentRepository.findById(commentReactionRequest.getCommentId());
	
		if(commentService.commentNotFound(response, c)) {
			return response.getResponseEntity();
		}
		
		User u = userService.getAuthenticatedUser();
		
		if(userService.compareTwoUser(response, c.get().getCommentAuthor(), u)) {
			return response.getResponseEntity();
		}
		
		if(commentService.isCommentBanned(response, c)) {
			return response.getResponseEntity();
		}
	
		CommentReactionId cri = new CommentReactionId(c.get(),u);
		Optional<CommentReaction> crr =commentReactionRepository.findById(cri);
		
		if(crr.isPresent()) {
			if(commentReactionService.compareReactionType(response, crr, commentReactionRequest.getReaction())) {
				return response.getResponseEntity();
				
			} else {
				crr.get().setReaction(commentReactionRequest.getReaction());
				response.setMsg("Reaction updated");
				response.setStatus(HttpStatus.OK);
				return response.getResponseEntity();
			}
		}
		
		CommentReaction cr = new CommentReaction(cri,commentReactionRequest.getReaction());
		commentReactionRepository.save(cr);
	
		response.setMsg("Reaction added");
		response.setStatus(HttpStatus.CREATED);
		return response.getResponseEntity();
	}
	
	@GetMapping("private/find-reaction-by-name")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> findReactionByName(@RequestParam String name, 
			HttpServletRequest request) {
		
		CommentReactionName commentReactionName = CommentReactionName.valueOf(name.toUpperCase());
		ResponseEntityHandler response = new ResponseEntityHandler(request);		
		
		
		List<CommentReactionResponse> reactionList = commentReactionRepository.findByReactionName(commentReactionName);
		
		response.setMsg(reactionList);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();
		
	}
	
	@GetMapping("private/find-reaction-by-comment")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> findReactionByComment(HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);		
		
		List<CommentReactionTotalResponse> reactionList = commentReactionRepository.findTotalReactionsByComment();
		
		
		response.setMsg(reactionList);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();	
		
	}
	
	@GetMapping("private/find-reaction-by-user")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> findReactionByUser(HttpServletRequest request) {
		ResponseEntityHandler response = new ResponseEntityHandler(request);	
		
	
		
		List<CommentReactionByUser> reactionList = commentReactionRepository.countReactionByUser();
		if(reactionList.isEmpty()) {
			response.setMsg("no comment with reaction");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
			
			
		response.setMsg(reactionList);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();	
	}
	
}
