package it.course.myblogc4.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class PurchaseReportResponse {
	
	private String username;
	private long totalGain;
	private long totalSpent;
	private long balance;
	
}
