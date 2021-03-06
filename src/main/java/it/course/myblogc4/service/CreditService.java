package it.course.myblogc4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.course.myblogc4.entity.User;
import it.course.myblogc4.repository.CommentRepository;
import it.course.myblogc4.repository.PurchasedPostRepository;

@Service
public class CreditService {

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	PurchasedPostRepository purchasedPostRepository;
	
	public Long getCommentsWrote(User user) {
		return commentRepository.countByCommentAuthor(user);
	}
	
	public Long getCommentsBanned(User user) {
		return commentRepository.getCountBannedComments(user);
	}
	
	public Long getCreditsSpent(User user) {
		return purchasedPostRepository.getTotalSpentCredit(user);
	}
	

	public Long getUserBalance(User user){
		return getCommentsWrote(user) - getCommentsBanned(user) - getCreditsSpent(user);
	}
	

}
	

