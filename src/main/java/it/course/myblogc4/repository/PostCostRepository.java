package it.course.myblogc4.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import it.course.myblogc4.entity.Post;
import it.course.myblogc4.entity.PostCost;
import it.course.myblogc4.entity.PostCostId;


@Repository
public interface PostCostRepository extends JpaRepository<PostCost,PostCostId>{
	
	Optional<PostCost> findByEndDateEqualsAndPostCostIdPost(Date endDate, Post id);

}
