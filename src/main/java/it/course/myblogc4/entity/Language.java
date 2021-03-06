package it.course.myblogc4.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="LANGUAGE")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Language {
	
	@Id
	@Column(name="LANG_CODE", columnDefinition="VARCHAR(2)")
	private String langCode;
	
	@Column(length=45, nullable=false, unique=true)
	private String langName;
	
	@Column(name="IS_VISIBLE", nullable=false, columnDefinition="TINYINT(1)")
	private boolean visible = true;

	public Language(String langCode, String langName) {
		super();
		this.langCode = langCode;
		this.langName = langName;
	}
	
	

}
