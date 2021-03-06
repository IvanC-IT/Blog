package it.course.myblogc4.controller;

import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc4.entity.Country;
import it.course.myblogc4.entity.MainLand;
import it.course.myblogc4.payload.request.CountryRequest;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.repository.CountryRepository;
import it.course.myblogc4.repository.MainLandRepository;

@RestController
@Validated
public class CountryController {
	
	@Autowired CountryRepository countryRepository;
	@Autowired MainLandRepository mainLandRepository;
	
	@PostMapping("private/add-country")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponseCustom> addCountry(@RequestBody @Valid CountryRequest countryRequest, HttpServletRequest request) {
		
		Optional<MainLand> mainLand = mainLandRepository.findByMainLandName(countryRequest.getMainLand());
		if(!mainLand.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
					new ApiResponseCustom(Instant.now(), 404, "NOT FOUND", "Mainland not found", request.getRequestURI()
						), HttpStatus.NOT_FOUND);
		
		Optional<Country> country = countryRepository.findById(countryRequest.getCountryCode());
		
		String msg = "";
		
		if(country.isPresent()) {
			country.get().setCountryName(countryRequest.getCountryName());
			country.get().setMainLand(new MainLand(countryRequest.getMainLand()));
			countryRepository.save(country.get());
			msg = "country "+countryRequest.getCountryCode()+" updated";
		} else {
			countryRepository.save(
				new Country(
					countryRequest.getCountryCode(),
					countryRequest.getCountryName(),
					new MainLand(countryRequest.getMainLand())
					)
				);
			msg = "New Country added";
		}
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", msg, request.getRequestURI()
					), HttpStatus.OK);
		
	}
	
	@GetMapping("public/get-country")
	public ResponseEntity<ApiResponseCustom> getCountry(@RequestParam @NotEmpty @NotBlank @Size(min=2, max=2) String countryCode, HttpServletRequest request) {
		
		Optional<Country> country = countryRepository.findById(countryCode);
		if(!country.isPresent())
			return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 404, "NOT FOUND", "Country not found", request.getRequestURI()
					), HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<ApiResponseCustom>(
				new ApiResponseCustom(Instant.now(), 200, "OK", country.get(), request.getRequestURI()
					), HttpStatus.OK);
		
	}
		
}