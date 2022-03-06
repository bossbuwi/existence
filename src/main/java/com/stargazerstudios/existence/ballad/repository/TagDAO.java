package com.stargazerstudios.existence.ballad.repository;

import com.stargazerstudios.existence.ballad.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagDAO extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    @Query("SELECT t1 FROM Tag t1 WHERE t1.name IN :names")
    List<Tag> findTagsByName(@Param("names") Set<String> names);
    @Query("SELECT t1.name FROM Tag t1 WHERE t1.name IN :names")
    List<String> findTagNames(@Param("names") Set<String> names);
}
