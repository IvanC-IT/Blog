package it.course.myblogc4.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class CommentReactionByUser {

	private String authorCommentReaction;
	private long nrReactionByName;
	
}
