package it.course.myblogc4.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class CommentRequest {
	
	@NotBlank @NotEmpty @Size(min=1, max=200)
	private String comment;
	
	@NotNull
	private long refererTo;

}
