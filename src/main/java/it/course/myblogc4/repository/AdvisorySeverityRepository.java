package it.course.myblogc4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.AdvisorySeverity;

@Repository
public interface AdvisorySeverityRepository extends JpaRepository<AdvisorySeverity, String>{
	
	
	boolean existsBySeverityValue(int value);
	
	List<AdvisorySeverity> findAllByOrderBySeverityValueAsc();
	

}
