package it.course.myblogc4.payload.request;

import com.sun.istack.NotNull;

import it.course.myblogc4.entity.CommentReactionName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class CommentReactionRequest {

	@NotNull
	private long commentId;

	@NotNull
	private CommentReactionName reaction;
	
	
}
