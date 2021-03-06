package it.course.myblogc4.payload.request;

import javax.validation.constraints.NotNull;

import it.course.myblogc4.entity.AdvisoryStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor 
public class AdvisoryIdRequest {
	
	@NotNull
	private long commentId;
	@NotNull
	private long userId;
	@NotNull
	private long advisoryReasonId;
	@NotNull
	private AdvisoryStatus status;
	
}
