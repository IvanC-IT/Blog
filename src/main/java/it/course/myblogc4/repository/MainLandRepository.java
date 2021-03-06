package it.course.myblogc4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.MainLand;

@Repository
public interface MainLandRepository extends JpaRepository<MainLand, String>{
	
	// JPA Keywords
	Optional<MainLand> findByMainLandName(String mainLand); // select * from main_land where main_land_name = 'Europe';
	
	// SQL
	@Query(value="SELECT * FROM mainland WHERE main_land_name = :mainLand", nativeQuery=true)
	MainLand getMainLandByNameSQL(@Param("mainLand") String mainLand);
	
	// JPQL: Java Persistent Query Language
	@Query(value="SELECT m FROM MainLand m WHERE m.mainLandName = :mainLand")
	MainLand getMainLandByNameJPQL(@Param("mainLand") String mainLand);
	

}
