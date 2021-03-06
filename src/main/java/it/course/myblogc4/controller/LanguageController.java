package it.course.myblogc4.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import it.course.myblogc4.entity.Language;
import it.course.myblogc4.payload.request.LanguageRequest;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.LanguageRepository;

@RestController
@Validated
public class LanguageController {
	
	@Autowired LanguageRepository languageRepository;
	
	@PostMapping("private/add-language")
	@PreAuthorize("hasRole('ADMIN')")
	ResponseEntity<ApiResponseCustom> addLanguage(HttpServletRequest request,
			@RequestBody @Valid LanguageRequest languageRequest) {
		
		String languageCode = languageRequest.getLangCode();
		
		Object msg;
		HttpStatus status;
		ResponseEntityHandler response;
		
		if(languageRepository.findById(languageCode).isPresent()){
			msg = "Language already presents";
			status = HttpStatus.OK;
		} else {
			String languageName = languageRequest.getLangName();
			languageRepository.save(new Language(languageCode, languageName));
			msg = "Language " + languageName + " added";
			status = HttpStatus.OK;	
		}
				
		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();
	}
	

	@GetMapping("public/get-languages")
	public ResponseEntity<?> getLanguages(HttpServletRequest request) {
		List<Language> languageList = languageRepository.findAll();
		
		Object msg;
		HttpStatus status;
		ResponseEntityHandler response;
		
		if(languageList.isEmpty()) {
			msg = "Resource not found";
			status = HttpStatus.NOT_FOUND;
		} else {
			msg = languageList;
			status = HttpStatus.OK;
		}
		
		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();		
	}
	
	@PutMapping("private/change-visibility")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> changeVisibility(HttpServletRequest request,
			@RequestParam String langCode) {
		
		Optional<Language> languageOptional = languageRepository.findById(langCode);
		
		Object msg;
		HttpStatus status;
		ResponseEntityHandler response;
		
		if(languageOptional.isPresent()) {
			Language languageEntity = languageOptional.get();
			languageEntity.setVisible(!languageEntity.isVisible());
			languageRepository.save(languageEntity);
			
			msg = "Visibilyt has been updated for language " + languageEntity.getLangName();
			status = HttpStatus.OK;
			
		} else {
			msg = "language "+ langCode + " not found";
			status = HttpStatus.NOT_FOUND;
		}
		
		response = new ResponseEntityHandler(msg, request, status);
		return response.getResponseEntity();	
	}

}
