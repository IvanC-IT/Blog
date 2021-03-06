package it.course.myblogc4.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.AdvisoryReason;
import it.course.myblogc4.entity.AdvisoryReasonDetail;
import it.course.myblogc4.entity.AdvisoryReasonDetailId;

@Repository
public interface AdvisoryReasonDetailRepository extends JpaRepository<AdvisoryReasonDetail, AdvisoryReasonDetailId> {
	
	Optional<AdvisoryReasonDetail> findByEndDateEqualsAndAdvisoryReasonDetailIdAdvisoryReason(Date endDate, AdvisoryReason ar);

	
	
}
