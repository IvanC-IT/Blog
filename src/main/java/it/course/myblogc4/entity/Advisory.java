package it.course.myblogc4.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import it.course.myblogc4.entity.audit.DateAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="ADVISORY")
@Getter @Setter @NoArgsConstructor
public class Advisory extends DateAudit{
	
	@EmbeddedId
	private AdvisoryId advisoryId;
	
	@Column(name="STATUS", length=27, nullable=false)
	@Enumerated(EnumType.STRING)
	private AdvisoryStatus advisoryStatus = AdvisoryStatus.OPEN;
	
	@Column(length=200)
	private String description;
	
	public Advisory(AdvisoryId advisoryId, String description) {
		super();
		this.advisoryId = advisoryId;
		this.description = description;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((advisoryId == null) ? 0 : advisoryId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Advisory))
			return false;
		Advisory other = (Advisory) obj;
		if (advisoryId == null) {
			if (other.advisoryId != null)
				return false;
		} else if (!advisoryId.equals(other.advisoryId))
			return false;
		return true;
	}

	
	
}
