package it.course.myblogc4.payload.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class CommentResponse {
	
	private long commentId;
	private String comment;
	private Date createdAt;
	private String commentAuthorName;
	private long refererTo;

}
