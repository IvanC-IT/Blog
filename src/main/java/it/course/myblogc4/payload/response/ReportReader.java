package it.course.myblogc4.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class ReportReader {
	
	private Long id;
	private String username;
	private long numberOfComments;
	private long numberOfBannedComment;
	private Boolean enabled;	
	

}
