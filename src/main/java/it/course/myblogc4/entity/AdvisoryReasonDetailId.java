package it.course.myblogc4.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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
public class AdvisoryReasonDetailId implements Serializable{
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ADVISORY_REASON", nullable=false)
	private AdvisoryReason advisoryReason;
	
	@Column(name="START_DATE", nullable=false, columnDefinition="DATE")
	private Date startDate;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((advisoryReason == null) ? 0 : advisoryReason.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AdvisoryReasonDetailId))
			return false;
		AdvisoryReasonDetailId other = (AdvisoryReasonDetailId) obj;
		if (advisoryReason == null) {
			if (other.advisoryReason != null)
				return false;
		} else if (!advisoryReason.equals(other.advisoryReason))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}
	
	private static final long serialVersionUID = 1L;

}
