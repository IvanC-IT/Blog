package it.course.myblogc4.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RatingId implements Serializable{
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RATER", nullable=false)
	private User rater;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="POST_ID", nullable=false)
	private Post post;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((post == null) ? 0 : post.hashCode());
		result = prime * result + ((rater == null) ? 0 : rater.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RatingId))
			return false;
		RatingId other = (RatingId) obj;
		if (post == null) {
			if (other.post != null)
				return false;
		} else if (!post.equals(other.post))
			return false;
		if (rater == null) {
			if (other.rater != null)
				return false;
		} else if (!rater.equals(other.rater))
			return false;
		return true;
	}

}
