package it.course.myblogc4.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import it.course.myblogc4.entity.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class CommentReaction  extends DateAudit{

	@EmbeddedId
	private CommentReactionId commentReactionId;
	

	@Column(nullable = false , columnDefinition = "VARCHAR(10)")
	@Enumerated(EnumType.STRING)
	private CommentReactionName reaction;
	
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commentReactionId == null) ? 0 : commentReactionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommentReaction other = (CommentReaction) obj;
		if (commentReactionId == null) {
			if (other.commentReactionId != null)
				return false;
		} else if (!commentReactionId.equals(other.commentReactionId))
			return false;
		return true;
	}	
}
