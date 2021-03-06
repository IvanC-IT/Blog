package it.course.myblogc4.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Comment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false, length=200)
	private String comment;
	
	@Column(name="CREATED_AT", 
		updatable=false, insertable=false, nullable=false,
		columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdAt;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMMENT_AUTHOR", nullable=false)
	private User commentAuthor;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="POST_ID", nullable=false)
	private Post post;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="REFERERS_TO", 
		joinColumns = {@JoinColumn(name="CHILD", referencedColumnName="ID")},
		inverseJoinColumns = {@JoinColumn(name="FATHER", referencedColumnName="ID")})
	Set<Comment> referersTo = new HashSet<Comment>();

	public Comment(String comment, User commentAuthor, Post post) {
		super();
		this.comment = comment;
		this.commentAuthor = commentAuthor;
		this.post = post;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Comment))
			return false;
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
