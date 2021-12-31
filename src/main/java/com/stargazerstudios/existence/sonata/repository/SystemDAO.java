package com.stargazerstudios.existence.sonata.repository;

import com.stargazerstudios.existence.sonata.entity.System;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemDAO extends JpaRepository<System, Long> {
    Optional<System> findByGlobalPrefix(String globalPrefix);
}
