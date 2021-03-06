package it.course.myblogc4.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;


import org.springframework.stereotype.Service;

@Service
public class AdvisoryService {
	
	
	public Date get9999Date() {
		
		LocalDate localDate = LocalDate.of(9999, Month.DECEMBER, 31);
		
		ZoneId zone = ZoneId.from(ZonedDateTime.now()); 
		Date x = Date.from(localDate.atStartOfDay()
			      .atZone(ZoneOffset.UTC)
			      .toInstant());
		
		System.out.println(" ZoneId ----------------> "+zone );
		System.out.println(" Date ------------------> "+x );
		
		return x;
		
	}

}
