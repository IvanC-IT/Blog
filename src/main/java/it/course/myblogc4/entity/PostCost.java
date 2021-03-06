package it.course.myblogc4.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="POST_COST")
@Setter@Getter@AllArgsConstructor@NoArgsConstructor
public class PostCost {

	@EmbeddedId
	PostCostId postCostId;
	
	@Column(name="END_DATE", nullable=false, columnDefinition="DATE")
	private Date endDate;
	
	@Column(columnDefinition = ("TINYINT(2)"))
	private int cost;

	
}
