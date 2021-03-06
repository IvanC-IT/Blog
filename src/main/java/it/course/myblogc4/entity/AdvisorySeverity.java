package it.course.myblogc4.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ADVISORY_SEVERITY")
@Data @AllArgsConstructor @NoArgsConstructor
public class AdvisorySeverity {
	
	@Id
	@Column(name="SEVERITY_DESCRIPTION", length=15) // LOW, MEDIUM, HIGH, HIGHEST
	private String severityDescription;
	
	@Column(name="SEVERITY_VALUE", nullable=false, unique=true, columnDefinition="SMALLINT(5)")
	private int severityValue; // 25 - 50 - 75  - 100

}
