package it.course.myblogc4.payload.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class PostVisitedResponse {

	private long postId;
	private long nrAnonymous;
	private long nrRegistered;
}
