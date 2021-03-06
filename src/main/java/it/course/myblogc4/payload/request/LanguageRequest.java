package it.course.myblogc4.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class LanguageRequest {
	
	@NotEmpty @NotBlank @Size(min=2,max=2) 
	private String langCode; 
	
	@NotEmpty @NotBlank @Size(min=2,max=45)
	private String langName;
}