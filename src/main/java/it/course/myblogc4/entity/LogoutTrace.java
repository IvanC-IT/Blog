package it.course.myblogc4.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LOGOUT_TRACE")
@Data@NoArgsConstructor
public class LogoutTrace {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(columnDefinition = "TEXT", unique = true)
	private String tokenNotValid;
	
	@Column(columnDefinition = "DATETIME",nullable = false)
	private Date expiration;

	public LogoutTrace(String tokenNotValid, Date expiration) {
		super();
		this.tokenNotValid = tokenNotValid;
		this.expiration = expiration;
	}
	
}
