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
public class AdvisoryId implements Serializable{
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COMMENT_ID")
	private Comment comment;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REPORTER")
	private User reporter;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADVISORY_REASON")
	private AdvisoryReason advisoryReason;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((advisoryReason == null) ? 0 : advisoryReason.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((reporter == null) ? 0 : reporter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AdvisoryId))
			return false;
		AdvisoryId other = (AdvisoryId) obj;
		if (advisoryReason == null) {
			if (other.advisoryReason != null)
				return false;
		} else if (!advisoryReason.equals(other.advisoryReason))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (reporter == null) {
			if (other.reporter != null)
				return false;
		} else if (!reporter.equals(other.reporter))
			return false;
		return true;
	}
	

	
}
