package it.course.myblogc4.payload.response;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PostMainLandResponse {
	
	private long id;
	private String title;
	private String overview;
	private Date updatedAt;
	private String author;
	private String langName;
	private Set<String> mainLandNames; 
	
}
