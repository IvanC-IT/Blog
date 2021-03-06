package it.course.myblogc4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.course.myblogc4.entity.CommentReaction;
import it.course.myblogc4.entity.CommentReactionId;
import it.course.myblogc4.entity.CommentReactionName;
import it.course.myblogc4.payload.response.CommentReactionByUser;
import it.course.myblogc4.payload.response.CommentReactionResponse;
import it.course.myblogc4.payload.response.CommentReactionTotalResponse;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, CommentReactionId>{

	@Query("SELECT new it.course.myblogc4.payload.response.CommentReactionResponse("
			+ "cr.commentReactionId.comment.post.id, "
			+ "cr.commentReactionId.comment.id,"
			+ "COUNT(cr.reaction)"
			+ ") "
			+ "FROM CommentReaction cr "
			+ "WHERE cr.reaction = :reaction "
			+ "GROUP BY cr.commentReactionId.comment.id "
			+ "ORDER BY COUNT(cr.reaction) DESC"
			)
	List<CommentReactionResponse> findByReactionName(@Param("reaction")CommentReactionName reaction);
	
	
	
	@Query("SELECT new it.course.myblogc4.payload.response.CommentReactionTotalResponse("
			+ "cr.commentReactionId.comment.id,"
			+ "COUNT(cr.reaction) AS nReaction"
			+ ") "
			+ "FROM CommentReaction cr "
			+ "GROUP BY cr.commentReactionId.comment.id "
			+ "ORDER BY nReaction DESC"
			)
	List<CommentReactionTotalResponse> findTotalReactionsByComment();
	

	@Query("SELECT new it.course.myblogc4.payload.response.CommentReactionByUser("
			+ "u.username,"
			+ "(SELECT COALESCE(COUNT(cr.reaction),0) FROM CommentReaction cr WHERE cr.commentReactionId.user = u ) AS nReaction"
			+ ") "
			+ "FROM CommentReaction cr "
			+ "RIGHT JOIN User u ON cr.commentReactionId.user = u "
			+ "JOIN u.authorities ua "
			+ "WHERE ua.name != it.course.myblogc4.entity.AuthorityName.ROLE_ADMIN "
			+ "GROUP BY u "
			+ "ORDER BY nReaction DESC")
	List<CommentReactionByUser> countReactionByUser();
}
