package it.course.myblogc4.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.PostVisited;
import it.course.myblogc4.payload.response.PostVisitedResponse;

@Repository
public interface PostVisitedRepository extends JpaRepository<PostVisited, Long>{
	
	@Query("SELECT new it.course.myblogc4.payload.response.PostVisitedResponse("
			+ "p.id, "
			+ "SUM(CASE WHEN pv.userId = 0 AND pv.createdAt <= :endDate THEN 1 ELSE 0 END), "
			+ "SUM(CASE WHEN pv.userId <> 0 AND pv.createdAt <= :endDate THEN 1 ELSE 0 END) "
			+ ") "
			+ "FROM PostVisited pv "
			+ "RIGHT JOIN Post p ON p = pv.post "
			+ "WHERE pv.createdAt >= :startDate "
			+ "OR pv.createdAt IS NULL "
			+ "GROUP BY p.id "
			+ "ORDER BY COUNT(pv.post) DESC")
	List<PostVisitedResponse> getViews(@Param("startDate")Date startDate, @Param("endDate")Date endDate);
}
