package it.course.myblogc4.controller;

import java.io.InputStream;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc4.entity.Post;
import it.course.myblogc4.repository.PostRepository;
import it.course.myblogc4.service.PdfFileService;

@RestController
public class PdfController {
	
	@Autowired PostRepository postRepository;
	@Autowired PdfFileService pdfFileService;
	
	
	@GetMapping("public/create-pdf/{id}")
	public ResponseEntity<?> createPdf(HttpServletRequest request, @PathVariable long id){
		
		
		Optional<Post> p = postRepository.findByIdAndPublishedTrue(id);
		
		
		InputStream pdfFile = null;
		ResponseEntity<InputStreamResource> responseEntity = null;
		
		try {
			
			// creazione del pdf - > metodo che metteremo nella classe PdfFileService.java
			pdfFile = pdfFileService.createPdfFromPost(p.get());
			
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			headers.add("Access-Control-Allow-Origin", "*");
			headers.add("Access-Control-Allow-Method", "GET");
			headers.add("Access-Control-Allow-Header", "Content-Type");
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Expires", "0");
			headers.add("Content-disposition", "inline; filename="+p.get().getTitle().replaceAll(" ", "_")+".pdf");
			
			responseEntity = new ResponseEntity<InputStreamResource>(new InputStreamResource(pdfFile), headers, HttpStatus.OK);
			
		} catch (Exception e) {
			
			responseEntity = new ResponseEntity<InputStreamResource>(new InputStreamResource( null, "Pdf creation failed: "+e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		return responseEntity;		
		
	}
	

}
