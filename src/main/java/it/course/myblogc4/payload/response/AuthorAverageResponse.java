package it.course.myblogc4.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class AuthorAverageResponse {
	
	private long authorId;
	private String authorName;
	private long postNumber; // comprende anche i post che non hanno ricevuto voti
	private double authorAverage;

}
