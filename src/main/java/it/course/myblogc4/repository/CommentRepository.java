package it.course.myblogc4.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.Comment;
import it.course.myblogc4.entity.User;
import it.course.myblogc4.payload.response.CommentResponse;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	@Query(value="SELECT new it.course.myblogc4.payload.response.CommentResponse("
			+ "c.id, "
			+ "CASE WHEN ad.advisoryStatus = 'CLOSED_WITH_CONSEQUENCE' "
			+ "THEN 'Comment removed' "
			+ "ELSE c.comment END, "
			+ "c.createdAt,"
			+ "c.commentAuthor.username, "
			+ "COALESCE(rt.id, 0L)) "
			+ "FROM Comment c "
			+ "LEFT JOIN c.referersTo rt "
			+ "LEFT JOIN Advisory ad "
			+ "ON ad.advisoryId.comment=c "
			+ "WHERE c.post.id = :postId "
			+ "ORDER BY c.createdAt ASC")
	List<CommentResponse> getCommentResponse(@Param("postId") long postId);
	
	
	@Query(value="SELECT c from Comment c "
			+ "JOIN FETCH c.commentAuthor "
			+ "WHERE c.id= :commentId")
	Optional<Comment> getCommentAndAuthor(@Param("commentId") long commentId);

	@Query(value = "SELECT COALESCE(COUNT(c.id),0)" +
			"FROM Comment c INNER JOIN Advisory a ON a.advisoryId.comment.id = c.id " +
			"WHERE c.commentAuthor = :u AND a.advisoryStatus = 'CLOSED_WITH_CONSEQUENCE' ")
	Long getCountBannedComments(@Param("u") User user);


	long countByCommentAuthor(User user);
}
