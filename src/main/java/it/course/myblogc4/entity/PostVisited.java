package it.course.myblogc4.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data@AllArgsConstructor@NoArgsConstructor
public class PostVisited {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="POST_ID" , nullable=false)
	private Post post;
	
	@Column(name="CREATED_AT", 
			updatable=false , insertable=false , nullable=false,
			columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
		private Date createdAt;
	

	public PostVisited(Post post, long userId, String ip, String device) {
		super();
		this.post = post;
		this.userId = userId;
		this.ip = ip;
		this.device = device;
	}

	@Column(name = "USER_ID" , nullable = false , columnDefinition = "BIGINT(20)")
	private long userId = 0;
	
	@Column(name = "IP" , nullable = false, columnDefinition = "VARCHAR(45)")
	private String ip;
	
	@Column(name = "DEVICE" , nullable = false)
	private String device = "NOT AVALAIBLE";
	
}
