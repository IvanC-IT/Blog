package it.course.myblogc4.payload.request;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class AdvisoryReasonRequest {
	
	@NotBlank @NotEmpty @Size(max=15, min=3) 
	private String advisoryReasonName;
	
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date startDate;
	
	@NotBlank @NotEmpty @Size(max=15, min=3)
	private String advisorySeverity;

}
