package it.course.myblogc4.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.Advisory;
import it.course.myblogc4.entity.AdvisoryId;
import it.course.myblogc4.entity.AdvisoryStatus;
import it.course.myblogc4.entity.Comment;


@Repository
public interface AdvisoryRepository extends JpaRepository<Advisory, AdvisoryId>{
	
	@Query (value = ("SELECT * FROM Advisory WHERE comment_id = :id") ,nativeQuery = true)
	List<Advisory> getAdvisory (@Param("id") long id);

	boolean existsByAdvisoryIdCommentAndAdvisoryStatus(Comment comment, AdvisoryStatus closedWithConsequence);
	
long countByAdvisoryStatusEquals(AdvisoryStatus status);
	
	@Query(value="SELECT COUNT(*)FROM advisory ad "
			+ "WHERE status= :status", nativeQuery=true)
	long countByAdvisoryStatusEqualsSQL(@Param("status") String status);
}
