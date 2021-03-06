package it.course.myblogc4.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class PostRequest {
	
	@NotEmpty @NotBlank @Size(min=1, max=100)
	private String title;
	
	@NotEmpty @NotBlank @Size(min=1, max=255)
	private String overview;
	
	@NotEmpty @NotBlank @Size(min=1, max=64000)
	private String content;
	
	@NotEmpty @NotBlank @Size(min=2, max=2)
	private String langCode;

}
