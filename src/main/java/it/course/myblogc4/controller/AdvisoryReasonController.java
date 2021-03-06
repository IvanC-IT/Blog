package it.course.myblogc4.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc4.entity.AdvisoryReason;
import it.course.myblogc4.entity.AdvisoryReasonDetail;
import it.course.myblogc4.entity.AdvisoryReasonDetailId;
import it.course.myblogc4.entity.AdvisorySeverity;
import it.course.myblogc4.payload.request.AdvisoryReasonRequest;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.ResponseEntityHandler;

import it.course.myblogc4.repository.AdvisoryReasonDetailRepository;
import it.course.myblogc4.repository.AdvisoryReasonRepository;
import it.course.myblogc4.repository.AdvisorySeverityRepository;
import it.course.myblogc4.service.AdvisoryService;

@RestController
@Validated
public class AdvisoryReasonController {
	
	@Autowired AdvisoryReasonRepository advisoryReasonRepository;
	@Autowired AdvisoryReasonDetailRepository advisoryReasonDetailRepository;
	@Autowired AdvisorySeverityRepository advisorySeverityRepository;
	@Autowired AdvisoryService advisoryService;
	
	
	@PostMapping("private/add-advisory-reason")
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ResponseEntity<ApiResponseCustom> addAdvisoryReason(
			@Valid @RequestBody AdvisoryReasonRequest advisoryReasonRequest, HttpServletRequest request){
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		
		Optional<AdvisoryReason> ar = advisoryReasonRepository.findByAdvisoryReasonName(advisoryReasonRequest.getAdvisoryReasonName());
		
		Optional<AdvisorySeverity> as = advisorySeverityRepository.findById(advisoryReasonRequest.getAdvisorySeverity().toUpperCase());
		if(!as.isPresent()) {
			response.setMsg("AdvisorySeverity is not present");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		AdvisoryReason arNew = new AdvisoryReason();
		
		if(ar.isPresent()) {			
			Optional<AdvisoryReasonDetail> ard = advisoryReasonDetailRepository.findByEndDateEqualsAndAdvisoryReasonDetailIdAdvisoryReason(advisoryService.get9999Date(), ar.get() );
			
			if(ard.get().getAdvisorySeverity().getSeverityDescription().equalsIgnoreCase(advisoryReasonRequest.getAdvisorySeverity())) {
				response.setMsg("You have to change the severity");
				response.setStatus(HttpStatus.FORBIDDEN);
				return response.getResponseEntity();
			}
			
			ard.get().setEndDate(DateUtils.addDays(advisoryReasonRequest.getStartDate(),-1));
			AdvisoryReasonDetail ardNew = new AdvisoryReasonDetail(
					new AdvisoryReasonDetailId (ar.get(), advisoryReasonRequest.getStartDate()), 
					advisoryService.get9999Date(),
					as.get()
					);
			response.setMsg("Advisory Reason updated");
			response.setStatus(HttpStatus.OK);
			advisoryReasonDetailRepository.save(ardNew);
		} else {
			arNew.setAdvisoryReasonName(advisoryReasonRequest.getAdvisoryReasonName().toUpperCase());
			advisoryReasonRepository.save(arNew);
			AdvisoryReasonDetail ardNew = new AdvisoryReasonDetail(
					new AdvisoryReasonDetailId (arNew, advisoryReasonRequest.getStartDate()), 
					advisoryService.get9999Date(),
					as.get()
					);
			response.setMsg("Advisory Reason created");
			response.setStatus(HttpStatus.CREATED);
			advisoryReasonDetailRepository.save(ardNew);
		}
		
		return response.getResponseEntity();
		
	}
	
	@GetMapping("private/get-advisory-reasons")
	public ResponseEntity<ApiResponseCustom> addAdvisoryReason(HttpServletRequest request){
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);

		List<AdvisoryReason> list = advisoryReasonRepository.getAllValidAdvisoryReason();
		
		if(list.isEmpty()) {
			response.setMsg("No advisory reasons present");
			response.setStatus(HttpStatus.NOT_FOUND);
			return response.getResponseEntity();
		}
		
		response.setMsg(list);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();
		
	}
	
}
		
