package it.course.myblogc4.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PostSearch {
	
	private long id;
	private String title;
	private String content;
	private String author;

}
