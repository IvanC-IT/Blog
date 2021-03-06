package it.course.myblogc4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.PurchasedPost;
import it.course.myblogc4.entity.PurchasedPostId;
import it.course.myblogc4.entity.User;
import it.course.myblogc4.payload.response.PostDetailResponse;
import it.course.myblogc4.payload.response.PurchaseReportResponse;

@Repository
public interface PurchasedPostRepository extends JpaRepository<PurchasedPost, PurchasedPostId> {


	@Query(value="SELECT COUNT (c) FROM Comment c "
			+ "WHERE c.commentAuthor = :user")
	int getUserCredit(@Param("user") User user);
	
	@Query(value = "SELECT COALESCE(COUNT(c.id),0)" +
			"FROM Comment c INNER JOIN Advisory a ON a.advisoryId.comment.id = c.id " +
			"WHERE c.commentAuthor = :u AND a.advisoryStatus = 'CLOSED_WITH_CONSEQUENCE' ")
	int getCountBannedComments(@Param("u") User user);
	
	@Query(value = "SELECT COALESCE(COUNT(p),0) "
			+ "FROM PurchasedPost p INNER JOIN User u ON u.id = p.purchasedPostId.user.id "
			+ "WHERE u = :u")
	int getpurchasedPost(@Param("u") User user);


	@Query(value = "SELECT COALESCE(SUM(pc.cost),0) " +
            "FROM PurchasedPost pp INNER JOIN PostCost pc  " +
            "ON pp.id.post.id = pc.id.post.id " +
            "AND pp.purchaseDate < pc.endDate " +
            "AND pp.purchaseDate > pc.id.startDate " +
            "WHERE pp.id.user = :u")
    Long getTotalSpentCredit(@Param("u") User u);
	
	
	@Query(value="SELECT new it.course.myblogc4.payload.response.PurchaseReportResponse("
			+ "u.username, "
			+ "(SELECT COALESCE(COUNT(*),0)"
			+ "FROM Comment c "
			+ "LEFT JOIN Advisory ad ON ad.advisoryId.comment=c "
			+ "WHERE c.commentAuthor = u AND "
			+ "(ad.advisoryStatus <> 'CLOSED_WITH_CONSEQUENCE' OR "
			+ "ad.advisoryStatus IS NULL)), "
			+ "(SELECT COALESCE(SUM(pc.cost),0) "
			+ "FROM PurchasedPost p "
			+ "LEFT JOIN PostCost pc ON pc.postCostId.post=p.purchasedPostId.post "
			+ "WHERE p.purchasedPostId.user = u AND "
			+ "cast(p.purchaseDate as date)>= pc.postCostId.startDate AND "
			+ "cast(p.purchaseDate as date)< pc.endDate), "
			+ ""
			+ "(SELECT COALESCE(COUNT(*),0)"
			+ "FROM Comment c "
			+ "LEFT JOIN Advisory ad ON ad.advisoryId.comment=c "
			+ "WHERE c.commentAuthor = u AND "
			+ "(ad.advisoryStatus <> 'CLOSED_WITH_CONSEQUENCE' OR "
			+ "ad.advisoryStatus IS NULL)) - "
			+ "(SELECT COALESCE(SUM(pc.cost),0) "
			+ "FROM PurchasedPost p "
			+ "LEFT JOIN PostCost pc ON pc.postCostId.post=p.purchasedPostId.post "
			+ "WHERE p.purchasedPostId.user = u AND "
			+ "cast(p.purchaseDate as date)>= pc.postCostId.startDate AND "
			+ "cast(p.purchaseDate as date)< pc.endDate)"
			+ ") "
			+ "FROM User u "
			+ "INNER JOIN u.authorities au ON au.name=it.course.myblogc4.entity.AuthorityName.ROLE_READER "
			+ "GROUP BY u")
	List<PurchaseReportResponse> purchaseReportResponse();
}
