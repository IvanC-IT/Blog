package it.course.myblogc4.controller;


import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc4.entity.Post;
import it.course.myblogc4.entity.PostCost;
import it.course.myblogc4.entity.PurchasedPost;
import it.course.myblogc4.entity.PurchasedPostId;
import it.course.myblogc4.payload.request.PostRequest;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.PurchaseReportResponse;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.PostCostRepository;
import it.course.myblogc4.repository.PostRepository;
import it.course.myblogc4.repository.PurchasedPostRepository;
import it.course.myblogc4.service.AdvisoryService;
import it.course.myblogc4.service.CreditService;
import it.course.myblogc4.service.UserService;

@RestController
public class PurchasedPostController {
	@Autowired UserService userService;
	@Autowired PurchasedPostRepository purchasedPostRepository;
	@Autowired CreditService creditService;
	@Autowired PostRepository postRepository;
	@Autowired PostCostRepository postCostRepository;
	@Autowired AdvisoryService dateService;

	 @PostMapping("/private/purchase-post")
	    @PreAuthorize("hasRole('READER')")
	    public ResponseEntity<ApiResponseCustom> purchasePost(@RequestParam long postId, HttpServletRequest request){

	        ResponseEntityHandler response = new ResponseEntityHandler(request);

	        Optional<Post> post = postRepository.findById(postId);
	        if(!post.isPresent()){
	            response.setMsg("Post not found");
	            response.setStatus(HttpStatus.NOT_FOUND);
	            return response.getResponseEntity();
	        }

	        if(purchasedPostRepository.existsById(new PurchasedPostId(userService.getAuthenticatedUser(), post.get()))){
	            response.setMsg("You have already purchased this post");
	            response.setStatus(HttpStatus.FORBIDDEN);
	            return response.getResponseEntity();
	        }

	        // calcolare i  commenti scritti dal reader - crediti
	        // sottrarre dai crediti i commenti bannati
	        Long userBalance = creditService.getUserBalance(userService.getAuthenticatedUser());

	        // recuperare il costo dal post della data attuale
	        Optional<PostCost> postCost = postCostRepository.findByEndDateEqualsAndPostCostIdPost(dateService.get9999Date(),post.get());
	        if(!postCost.isPresent()){
	            response.setMsg("Post not for sale");
	            response.setStatus(HttpStatus.FORBIDDEN);
	            return response.getResponseEntity();
	        }

	        if(userBalance < postCost.get().getCost()){
	            response.setMsg("Insufficient funds");
	            response.setStatus(HttpStatus.FORBIDDEN);
	            return response.getResponseEntity();
	        }

	        
	        PurchasedPost purchasedPost = new PurchasedPost(new PurchasedPostId(userService.getAuthenticatedUser(),post.get()));
	        purchasedPostRepository.save(purchasedPost);

	        response.setMsg("Post "+postId+" purchased");
	        return response.getResponseEntity();
	    }
	 

		@GetMapping("private/report-purchase-post")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<ApiResponseCustom> addPost(@RequestBody @Valid PostRequest postRequest,
				HttpServletRequest request){
		ResponseEntityHandler response = new ResponseEntityHandler(request);

		
		List<PurchaseReportResponse> list =purchasedPostRepository.purchaseReportResponse();
		
        response.setMsg(list);
        return response.getResponseEntity();
		}
}
