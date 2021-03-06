package it.course.myblogc4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.course.myblogc4.entity.DbFile;

public interface DbFileRepository extends JpaRepository<DbFile, Long>{

}
