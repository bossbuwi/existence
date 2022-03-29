package com.stargazerstudios.existence.sonata.repository;

import com.stargazerstudios.existence.sonata.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventTypeDAO extends JpaRepository<EventType, Long> {
    Optional<EventType> findByCode(String code);
    @Query("SELECT et FROM EventType et WHERE et.code IN :codes")
    List<EventType> findByCodeSet(@Param("codes")Set<String> codes);
}
