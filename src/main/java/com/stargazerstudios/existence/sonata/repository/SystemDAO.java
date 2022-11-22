package com.stargazerstudios.existence.sonata.repository;

import com.stargazerstudios.existence.sonata.entity.System;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemDAO extends JpaRepository<System, Long> {
    @Query("SELECT s FROM System s WHERE s.globalPrefix = :globalPrefix and s.machine.name = :name")
    Optional<System> findSystemOnMachine(@Param("globalPrefix") String globalPrefix, @Param("name") String machine);

    @Query("SELECT s FROM System s WHERE EXISTS (SELECT 1 FROM Event e WHERE e.system = s.id)")
    List<System> findSystemsWithEvents();
}
