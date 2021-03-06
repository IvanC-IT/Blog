package it.course.myblogc4.payload.response;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PostDetailResponse {
	
	private Long id;
	private String title;
	private String content;
	private Date updatedAt;
	private String author;
	private String langName;
	private List<CommentResponse> comments;
	private double average;
	
	public PostDetailResponse(Long id, String title, String content, Date updatedAt, String author, String langName,
			double average) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.updatedAt = updatedAt;
		this.author = author;
		this.langName = langName;
		this.average = average;
	}	
}
