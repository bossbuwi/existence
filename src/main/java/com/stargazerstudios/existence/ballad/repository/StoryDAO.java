package com.stargazerstudios.existence.ballad.repository;

import com.stargazerstudios.existence.ballad.entity.Story;
import com.stargazerstudios.existence.ballad.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StoryDAO extends JpaRepository<Story, Long> {
    Optional<Story> findByName(String name);
    @Query("SELECT s1 FROM Story s1 WHERE s1.name IN :names")
    List<Story> findStoriesByName(@Param("names") Set<String> names);
    @Query("SELECT s1.name FROM Story s1 WHERE s1.name IN :names")
    List<String> findStoryNames(@Param("names") Set<String> names);
}
