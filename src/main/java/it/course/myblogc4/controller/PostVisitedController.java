package it.course.myblogc4.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc4.entity.PostVisited;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.PostVisitedResponse;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.PostVisitedRepository;

@RestController
public class PostVisitedController {
	
	@Autowired PostVisitedRepository postVisitedRepository;
	
	@GetMapping("public/get-views")
	public ResponseEntity<ApiResponseCustom> getPostDetail(HttpServletRequest request,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  @RequestParam Date startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd")  @RequestParam Date endDate) {
		
		ResponseEntityHandler response = new ResponseEntityHandler(request);
		
		List<PostVisitedResponse> ls = postVisitedRepository.getViews(startDate, endDate);
		
		response.setMsg(ls);
		response.setStatus(HttpStatus.OK);
		return response.getResponseEntity();
	}
}
