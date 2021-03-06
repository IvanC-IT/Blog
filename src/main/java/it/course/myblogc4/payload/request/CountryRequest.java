package it.course.myblogc4.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CountryRequest {
	
	@NotEmpty @NotBlank @Size(min=2, max=2)
	private String countryCode;
	
	@NotEmpty @NotBlank @Size(min=2, max=45)
	private String countryName;
	
	@NotEmpty @NotBlank @Size(min=2, max=15)
	private String mainLand;

}
