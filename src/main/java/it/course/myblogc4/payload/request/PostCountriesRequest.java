package it.course.myblogc4.payload.request;

import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class PostCountriesRequest {

	@NotNull
	private long id;
	@NotEmpty
	private Set<String> countriesCode;
}
