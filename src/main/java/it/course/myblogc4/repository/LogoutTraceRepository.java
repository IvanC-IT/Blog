package it.course.myblogc4.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.LogoutTrace;

@Repository
public interface LogoutTraceRepository extends JpaRepository<LogoutTrace, Long>{
	
	Optional<LogoutTrace> findByTokenNotValid(String token);
	
	@Transactional
	@Modifying
    @Query(value="DELETE FROM logout_trace t WHERE t.expiration < CURRENT_TIMESTAMP", nativeQuery=true)
    void deleteExpiredTokens();

}
