package it.course.myblogc4.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ReportPost {
	
	private long id;
	
	private String title;
	
	private String author;
	
	private double avg;
	
	private Boolean published;
	
	private Boolean approved;
	
	private long visits;

}
