package it.course.myblogc4.payload.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PostCountCommentsResponse {
	
	private long id;
	private String title;
	private String overview;
	private Date updatedAt;
	private String author;
	private long commentsNumber ;
	
}
