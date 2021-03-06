package it.course.myblogc4.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ReportAuthor {
	
	private long id;
	private String username;
	private long nrWrittenPosts;
	private long nrViews;
	private double avgWrittenPosts;
	
}
