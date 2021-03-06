package it.course.myblogc4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.Rating;
import it.course.myblogc4.entity.RatingId;
import it.course.myblogc4.payload.response.AuthorAverageResponse;


@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId>{

	@Query(value="SELECT new it.course.myblogc4.payload.response.AuthorAverageResponse("
		+ "u.id,"
		+ "u.username,"
		+ "(SELECT COALESCE(COUNT(p.id),0) FROM Post p WHERE p.author.username = :authorName),"
		+ "(SELECT COALESCE(ROUND(AVG(r.rate),2),0) from Rating r WHERE r.ratingId.post.author.username = :authorName AND r.ratingId.rater.enabled = true)"
		+ ") "
		+ "FROM User u "
		+ "WHERE u.username = :authorName "
		)
	AuthorAverageResponse getAuthorAverage(@Param("authorName") String authorName);

	@Query(value="SELECT AVG(rate) FROM rating WHERE post_id=:id", nativeQuery=true)
	Double postAvg(@Param("id") long id);
	
}
