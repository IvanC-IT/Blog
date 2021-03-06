package it.course.myblogc4.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.course.myblogc4.entity.CommentReaction;
import it.course.myblogc4.entity.CommentReactionName;
import it.course.myblogc4.payload.response.ResponseEntityHandler;

@Service
public class CommentReactionService {

	public boolean compareReactionType(ResponseEntityHandler response, Optional<CommentReaction> cr, CommentReactionName reaction) {
		if(cr.get().getReaction()== reaction) {
			response.setMsg("Reaction already set");
			response.setStatus(HttpStatus.BAD_REQUEST);
			return true;
				
		} else {
			return false;
		}
	}
	
}
