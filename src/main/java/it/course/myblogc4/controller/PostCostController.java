package it.course.myblogc4.controller;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblogc4.entity.Post;
import it.course.myblogc4.entity.PostCost;
import it.course.myblogc4.entity.PostCostId;
import it.course.myblogc4.payload.response.ApiResponseCustom;
import it.course.myblogc4.payload.response.ResponseEntityHandler;
import it.course.myblogc4.repository.PostCostRepository;
import it.course.myblogc4.repository.PostRepository;
import it.course.myblogc4.service.AdvisoryService;
import it.course.myblogc4.service.UserService;

@RestController
@Validated
public class PostCostController {
	
	@Autowired PostRepository postRepository;
	@Autowired PostCostRepository postCostRepository;
	@Autowired AdvisoryService advisoryService;
	@Autowired UserService userservice;
	

	@PostMapping("/private/add-cost-to-post")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseCustom> addPostCost(
    		@RequestParam @Min(0) @Max(99) int cost,
            @RequestParam Long postId,
            HttpServletRequest request){
        ResponseEntityHandler response = new ResponseEntityHandler(request);

        Optional<Post> post = postRepository.findById(postId);
        if(!post.isPresent()){
            response.setMsg("Post not found");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }
        Date adjustedDate = userservice.adjustDate(new Date());
        
        Optional<PostCost> postCost = postCostRepository.findByEndDateEqualsAndPostCostIdPost(advisoryService.get9999Date(),post.get());
        if(postCost.isPresent()){
            if(postCost.get().getCost() == cost){
                response.setMsg("The post cost must be different");
                response.setStatus(HttpStatus.FORBIDDEN);
                return response.getResponseEntity();
            }
            postCost.get().setEndDate(adjustedDate);
            postCostRepository.save(postCost.get());
        }

        PostCostId postCostId = new PostCostId(post.get(),adjustedDate);
        PostCost newPostCost = new PostCost(postCostId,advisoryService.get9999Date(),cost);
        postCostRepository.save(newPostCost);

        response.setMsg("Cost added to post");
        return response.getResponseEntity();
    }



}