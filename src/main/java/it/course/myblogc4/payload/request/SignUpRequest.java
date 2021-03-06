package it.course.myblogc4.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SignUpRequest {
	
	@Email
	@Size(max=120, min=6)
	@NotBlank @NotEmpty
	private String email;
	
	@Size(max=12, min=3)
	@NotBlank @NotEmpty
	private String username;
	
	@Size(min=5, max=15)
	@NotBlank @NotEmpty
	private String password;

}
