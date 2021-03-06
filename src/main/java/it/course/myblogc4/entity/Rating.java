package it.course.myblogc4.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Check;

import it.course.myblogc4.entity.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="RATING")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Check(constraints= "RATE > 0 AND RATE < 6")
public class Rating extends DateAudit{

	@EmbeddedId
	private RatingId ratingId;
	
	@Column(name="RATE", nullable=false, columnDefinition="TINYINT(1)")
	private int rate;
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + rate;
		result = prime * result + ((ratingId == null) ? 0 : ratingId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Rating))
			return false;
		Rating other = (Rating) obj;
		if (rate != other.rate)
			return false;
		if (ratingId == null) {
			if (other.ratingId != null)
				return false;
		} else if (!ratingId.equals(other.ratingId))
			return false;
		return true;
	}


	private static final long serialVersionUID = 1L;
	
}
