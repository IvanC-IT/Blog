package it.course.myblogc4.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.Authority;
import it.course.myblogc4.entity.AuthorityName;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    
	Optional<Authority> findByName(AuthorityName name);
	
	Set<Authority> findByNameIn(Set<AuthorityName> authorityNames);

}
