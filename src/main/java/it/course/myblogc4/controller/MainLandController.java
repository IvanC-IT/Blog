package it.course.myblogc4.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc4.entity.MainLand;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.MainLandRepository;

@RestController
@Validated
public class MainLandController {
	
	@Autowired MainLandRepository mainLandRepository;
	
	/*
	//  ..../search?q=pippo -> @RequestParam	
	@GetMapping("get-mainland-by-name-1")
	public Optional<MainLand> getMainLand1(@RequestParam String mainLand) {
		
		Optional<MainLand> m = mainLandRepository.findByMainLandName(mainLand);
		
		return m;
	}
	
	//  ..../search/pippo   -> @PathVariable
	@GetMapping("get-mainland-by-name-2/{mainLand}")
	public MainLand getMainLand2(@PathVariable String mainLand) {
		
		MainLand m = mainLandRepository.getMainLandByNameSQL(mainLand);
		if(m != null)
			return m;
		
		return null;
	}
	*/
	
	//  ..../search/pippo   -> @PathVariable
	@GetMapping("public/get-mainland-by-name-3/{mainLandName}")
	public ResponseEntity<ApiResponseCustom> getMainLand3(HttpServletRequest request,
			@PathVariable String mainLandName) {
		
		MainLand mainLand = mainLandRepository.getMainLandByNameJPQL(mainLandName);
		
		Object msg;
		HttpStatus status;
		ResponseEntityHandler response;
		
		if(mainLand != null) {
			msg = mainLand;
			status = HttpStatus.OK;
		
		} else {
			msg = "Mainland not presents";
			status = HttpStatus.NOT_FOUND;
		}
		
		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();	
	}
	
	@PostMapping("private/add-mainland")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> addMainland(HttpServletRequest request,
			@RequestParam @NotEmpty @NotBlank @Size(min=2, max=15) String mainLandName) {
		
		Object msg;
		HttpStatus status;
		ResponseEntityHandler response;
		
		MainLand mainLand = mainLandRepository.getMainLandByNameJPQL(mainLandName);
		
		if(mainLand == null){
			mainLandRepository.save(new MainLand(mainLandName));
			msg = "new mainland added";
			
		} else {
			msg = "Mainland already present";
		}
		
		status = HttpStatus.OK;
		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();		
	}

}