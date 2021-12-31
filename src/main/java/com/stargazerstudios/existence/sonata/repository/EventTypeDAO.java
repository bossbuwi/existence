package com.stargazerstudios.existence.sonata.repository;

import com.stargazerstudios.existence.sonata.entity.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventTypeDAO extends JpaRepository<EventType, Long> {
    Optional<EventType> findByCode(String code);
}
