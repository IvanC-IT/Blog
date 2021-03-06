package it.course.myblogc4.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.DbFile;
import it.course.myblogc4.entity.User;
import it.course.myblogc4.payload.response.ReportAuthor;
import it.course.myblogc4.payload.response.ReportReader;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByEmail(String email);	
	
	Optional<User> findByUsernameOrEmail(String username, String email);
	
	Boolean existsByUsernameOrEmail(String username, String email);
	
	List<User> findAllByEnabledTrue();
	
	Optional<User> findByIdAndEnabledTrue(Long id);
	
	Optional<User> findByUsername(String username);
	Optional<User> findByUsernameAndEnabledTrue(String username);
    
    Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);	
	
	/*
	@Query(value="SELECT u.email, u.username, authorities.name " + 
			"from user u inner join user_authorities on u.id=user_authorities.user_id " + 
			"inner join authorities on authorities.id = user_authorities.authority_id " + 
			"where u.id=:id", nativeQuery=true)
	List<Object> getUser(@Param("id") long id);
	*/
	@Transactional
	@Modifying
	@Query(value="UPDATE user u SET "
			+ "u.banned_until = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL :banDays DAY), "
			+ "is_enabled = 0 "
			+ "WHERE u.id=:userId", nativeQuery=true)
	void updateBannedUntil(@Param("userId") long userId, @Param("banDays") int banDays);
	
	@Query(value="SELECT u " + 
			"FROM User u INNER JOIN u.authorities ua "
			+"WHERE ua.name = :role " 
			 
			)
	List<User> getUserByRole(@Param("role") String role);
	
	@Query(value = "SELECT "
			+ "new it.course.myblogc4.payload.response.ReportAuthor("
				+ "u.id,"
				+ "u.username, "
				+ "(SELECT COUNT(p.id) FROM Post p WHERE p.author.id = u.id), "
				+ "(SELECT COUNT(pv.id) FROM PostVisited pv WHERE pv.post.author.id = u.id), "
				+ "(SELECT COALESCE(ROUND(AVG(r.rate),2),0) FROM Rating r where r.ratingId.post.author.id = u.id) "
			+ ")"
			+ "FROM User u " 
			+ "INNER JOIN u.authorities ua ON ua.name='ROLE_EDITOR' "
			+ "ORDER BY u.username")
	List<ReportAuthor> getReportAuthor();
	
	@Query(value = "SELECT "
			+ "new it.course.myblogc4.payload.response.ReportReader("
				+ "u.id,"
				+ "u.username, "
				+ "(SELECT COUNT(c) FROM Comment c WHERE c.commentAuthor.id = u.id), "
				+ "(SELECT COUNT(ad) FROM Advisory ad WHERE ad.advisoryId.comment.commentAuthor.id = u.id AND ad.advisoryStatus='CLOSED_WITH_CONSEQUENCE'), "
				+ "u.enabled"
			+ ")"
			+ "FROM User u " 
			+ "INNER JOIN u.authorities ua ON ua.name='ROLE_READER' "
			+ "ORDER BY u.username")
	List<ReportReader> getReportReader();
	
	@Query(value="SELECT p.author.username "
			+ "FROM Post p "
			+ "WHERE p.id = :postId ")
	String getAuthorUsernameByPostId(long postId);
}