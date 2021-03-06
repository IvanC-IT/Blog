package it.course.myblogc4.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ADVISORY_REASON")
@Data @AllArgsConstructor @NoArgsConstructor
public class AdvisoryReason {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="ADVISORY_REASON_NAME", nullable=false, unique=true, length=20)
	private String advisoryReasonName;

	public AdvisoryReason(String advisoryReasonName) {
		super();
		this.advisoryReasonName = advisoryReasonName;
	}

	
	
	
}

/*

ReasonName = INSULTI ; id =1

//insert
ReasonDetail : reasonNameId(1); startdate= '2020-01-01' ; enddate= '9999-12-31'; severity= 'medium'

///update
ReasonDetail : reasonNameId(1); startdate= '2020-01-01' ; enddate= '2020-12-21'; severity= 'low'
// insert
ReasonDetail : reasonNameId(1); startdate= '2020-12-21' ; enddate= '9999-12-31'; severity= 'low'

*/












