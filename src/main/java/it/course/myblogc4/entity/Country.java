package it.course.myblogc4.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name="COUNTRY")
public class Country {
	
	@Id
	@Column(name="COUNTRY_CODE", columnDefinition="VARCHAR(2)")
	private String countryCode;
	
	@Column(length=45, nullable=false, unique=true)
	private String countryName;
	
	@ManyToOne
	@JoinColumn(name="MAIN_LAND", nullable=false)
	private MainLand mainLand;


}
