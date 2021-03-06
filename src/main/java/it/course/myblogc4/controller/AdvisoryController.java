package it.course.myblogc4.controller;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc4.entity.Advisory;
import it.course.myblogc4.entity.AdvisoryId;
import it.course.myblogc4.entity.AdvisoryReason;
import it.course.myblogc4.entity.AdvisoryReasonDetail;
import it.course.myblogc4.entity.AdvisoryStatus;
import it.course.myblogc4.entity.Comment;
import it.course.myblogc4.entity.User;
import it.course.myblogc4.payload.request.AdvisoryIdRequest;
import it.course.myblogc4.payload.request.AdvisoryRequest;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.AdvisoryReasonDetailRepository;
import it.course.myblogc4.repository.AdvisoryReasonRepository;
import it.course.myblogc4.repository.AdvisoryRepository;
import it.course.myblogc4.repository.CommentRepository;
import it.course.myblogc4.repository.UserRepository;
import it.course.myblogc4.service.AdvisoryService;
import it.course.myblogc4.service.UserService;

@RestController
public class AdvisoryController {
	
	@Autowired AdvisoryReasonDetailRepository advisoryReasonDetailRepository;
	@Autowired AdvisoryReasonRepository advisoryReasonRepository;
	@Autowired AdvisoryRepository advisoryRepository;
	@Autowired UserService userService;
	@Autowired UserRepository userRepository;
	@Autowired CommentRepository commentRepository;
	@Autowired AdvisoryService advisoryService;
	
	@PostMapping("private/add-advisory")	
	public ResponseEntity<ApiResponseCustom> addAdvisory(@Valid @RequestBody AdvisoryRequest advisoryRequest, HttpServletRequest request){
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		Optional<Comment> c = commentRepository.getCommentAndAuthor(advisoryRequest.getCommentId());
		if(!c.isPresent()) {
			response.setMsg("Comment does not exist");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		User u = userService.getAuthenticatedUser();
		
		if(u.getId() == c.get().getCommentAuthor().getId()) {
			response.setMsg("You cannot report yourself !");
			response.setStatus(HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}

		Optional<AdvisoryReason> ar = advisoryReasonRepository.getAdvisoryReason(advisoryRequest.getAdvisoryReasonId(), advisoryService.get9999Date());
		if(!ar.isPresent()){
			response.setMsg("Reason does not exist");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}

		AdvisoryId aId = new AdvisoryId(c.get(), u, ar.get());
		if(advisoryRepository.existsById(aId)) {
			response.setMsg("You alredy reported this comment for the same reason");
			response.setStatus(HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}

		Advisory a = new Advisory(aId, advisoryRequest.getDescription());
		advisoryRepository.save(a);

		response.setMsg("New Advisory added");

		return response.getResponseEntity();
	}
	
	@PutMapping("private/change-status-advisory")	
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> addAdvisory(@Valid @RequestBody AdvisoryIdRequest advisoryIdRequest, HttpServletRequest request){
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		Optional<Comment> c = commentRepository.findById(advisoryIdRequest.getCommentId());
		if(!c.isPresent()) {
			response.setMsg("No comment found");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		Optional<User> u = userRepository.findById(advisoryIdRequest.getUserId());
		if(!u.isPresent()) {
			response.setMsg("No user found");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		Optional<AdvisoryReason> ar = advisoryReasonRepository.findById(advisoryIdRequest.getAdvisoryReasonId());
		if(!ar.isPresent()) {
			response.setMsg("No advisory reason found");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		

		Optional<Advisory> a = advisoryRepository.findById(new AdvisoryId(c.get(), u.get(), ar.get()));
		
		if(a.get().getAdvisoryStatus().equals(advisoryIdRequest.getStatus())) {
			response.setMsg("No new status to update");
			response.setStatus(HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}
		
		/*
		 * CHANGE STATUS
		 * DA OPEN A PROGRESS
		 * DA OPEN A 1 DEI DUE CLOSE
		 * DA PROGRESS A 1 DEI DUE CLOSE
		 */
		
		if(a.get().getAdvisoryStatus() == AdvisoryStatus.CLOSED_WITH_CONSEQUENCE || 
				a.get().getAdvisoryStatus() == AdvisoryStatus.CLOSED_WITHOUT_CONSEQUENCE){
			response.setMsg("Advisory already closed");
			response.setStatus(HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}
		
		if(a.get().getAdvisoryStatus().compareTo(advisoryIdRequest.getStatus()) > 0){
			response.setMsg("Invalid advisory status for this instance");
			response.setStatus(HttpStatus.FORBIDDEN);
			return response.getResponseEntity();
		}
		
		

		if(advisoryIdRequest.getStatus() == AdvisoryStatus.CLOSED_WITH_CONSEQUENCE) {
			// update banned_until in  table user: now + severity_value
			Optional<AdvisoryReasonDetail> ard = advisoryReasonDetailRepository.findByEndDateEqualsAndAdvisoryReasonDetailIdAdvisoryReason(advisoryService.get9999Date(), ar.get() );
			int banDays = ard.get().getAdvisorySeverity().getSeverityValue();
			userRepository.updateBannedUntil(c.get().getCommentAuthor().getId(), banDays);
		}
		
		a.get().setAdvisoryStatus(advisoryIdRequest.getStatus());
		advisoryRepository.save(a.get());
		response.setMsg("Advisory status has been updated");
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();
		
	}
	

}
