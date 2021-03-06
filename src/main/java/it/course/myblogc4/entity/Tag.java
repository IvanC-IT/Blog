package it.course.myblogc4.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Tag {
	
	@Id
	@Column(name="TAG_NAME", length=20)
	private String tagName;
	

}
