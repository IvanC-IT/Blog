package it.course.myblogc4.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.AdvisoryReason;

@Repository
public interface AdvisoryReasonRepository extends JpaRepository<AdvisoryReason, Long>{
	
	Optional<AdvisoryReason> findByAdvisoryReasonName(String advisoryReasonName);
	/*
	@Query(value="SELECT * FROM advisory_reason ar "
			+ "INNER JOIN  advisory_reason_detail ard WHERE ar.id=ard.advisory_reason "
			+ "AND ard.end_date = :endDate", nativeQuery=true)
	List<AdvisoryReason> getValidAdvisoryReason(@Param("endDate") Date endDate);
	*/
	@Query("SELECT r FROM AdvisoryReason r "
			+ "INNER JOIN AdvisoryReasonDetail d ON r.id = d.advisoryReasonDetailId.advisoryReason.id "
			+ "WHERE d.endDate = '9999-12-31'")
	List<AdvisoryReason> getAllValidAdvisoryReason();
	
	@Query("SELECT r FROM AdvisoryReason r "
			+ "INNER JOIN AdvisoryReasonDetail d ON r.id = d.advisoryReasonDetailId.advisoryReason.id "
			+ "WHERE d.endDate = :endDate "
			+ "AND r.id = :advisoryId")
	Optional<AdvisoryReason> getAdvisoryReason(@Param("advisoryId") long advisoryId, @Param("endDate") Date endDate);




}
