package it.course.myblogc4.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ADVISORY_REASON_DETAIL")
@Data @AllArgsConstructor @NoArgsConstructor
public class AdvisoryReasonDetail {
	
	@EmbeddedId
	AdvisoryReasonDetailId advisoryReasonDetailId;
	
	@Column(name="END_DATE", nullable=false, columnDefinition="DATE")
	private Date endDate;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ADVISORY_SEVERITY", nullable=false)
	private AdvisorySeverity advisorySeverity;
	
	

}
