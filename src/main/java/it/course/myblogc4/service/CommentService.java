package it.course.myblogc4.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.course.myblogc4.entity.AdvisoryStatus;
import it.course.myblogc4.entity.Comment;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.AdvisoryRepository;
import it.course.myblogc4.repository.CommentRepository;

@Service
public class CommentService {

	@Autowired CommentRepository commentRepository;
	@Autowired AdvisoryRepository advisoryRepository;
	
	public boolean commentNotFound(ResponseEntityHandler response, Optional<Comment> c) {
		if(!c.isPresent()) {
			response.setMsg("Comment does not exists");
			response.setStatus(HttpStatus.BAD_REQUEST);
			return true;
		}
		
		return false;
	}
	
	public boolean isCommentBanned(ResponseEntityHandler response, Optional<Comment> c) {
		if(advisoryRepository.existsByAdvisoryIdCommentAndAdvisoryStatus(
				c.get(),
				AdvisoryStatus.CLOSED_WITH_CONSEQUENCE)) {
			
			response.setMsg("Banned comment");
			response.setStatus(HttpStatus.BAD_REQUEST);
			return true;
		}
		
		return false;
	}
}
