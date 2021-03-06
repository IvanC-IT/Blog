package it.course.myblogc4.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, String>{
	
	
	Set<Country> findByCountryCodeIn(Set<String> countriesCode);

}
