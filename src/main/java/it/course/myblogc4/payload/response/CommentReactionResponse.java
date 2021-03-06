package it.course.myblogc4.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommentReactionResponse {
	
	private long postId;
	private long commentId;
	private long nrReactionByName;

}
