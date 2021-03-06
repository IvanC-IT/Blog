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
@Table(name="PURCHASED_POST")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PurchasedPost {
	
	@EmbeddedId
	private PurchasedPostId purchasedPostId;
	
	public PurchasedPost(PurchasedPostId purchasedPostId) {
		super();
		this.purchasedPostId = purchasedPostId;
	}

	@Column(name="PURCHASE_DATE", 
		updatable=false, insertable=false, nullable=false,
		columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date purchaseDate;

}
