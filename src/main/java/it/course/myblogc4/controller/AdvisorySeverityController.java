package it.course.myblogc4.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import it.course.myblogc4.entity.AdvisorySeverity;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.AdvisorySeverityRepository;

@RestController
@Validated
public class AdvisorySeverityController {
	
	@Autowired AdvisorySeverityRepository advisorySeverityRepository;
	
	
	@PostMapping("private/add-advisory-severity")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> addSeverity(
			@RequestParam  @NotBlank @NotEmpty @Size(max=15, min=1)String severityName, 
			@RequestParam @NotNull @Digits(integer = 3, fraction = 0) int severityValue, HttpServletRequest request){
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		boolean existsSeverityName = advisorySeverityRepository.existsById(severityName);
		
		if(existsSeverityName) {
			response.setMsg("Advisory severity already present");
			response.setStatus(HttpStatus.OK);
			return response.getResponseEntity();
		}
		
		boolean existsSeverityValue = advisorySeverityRepository.existsBySeverityValue(severityValue);
		if(existsSeverityValue) {
			response.setMsg("Advisory value already present");
			response.setStatus(HttpStatus.OK);
			return response.getResponseEntity();
		}

		advisorySeverityRepository.save(new AdvisorySeverity(severityName.toUpperCase(),severityValue));
		
		response.setMsg("New severity "+severityName+" with value : "+severityValue);
		response.setStatus(HttpStatus.CREATED);
		return response.getResponseEntity();
	}
	
	@GetMapping("private/get-severities")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> getSeverities(HttpServletRequest request){
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		List<AdvisorySeverity> list = advisorySeverityRepository.findAllByOrderBySeverityValueAsc();
		if(list.isEmpty()) {
			response.setMsg("No Advisory Severity found");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		response.setMsg(list);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();
	}
}
