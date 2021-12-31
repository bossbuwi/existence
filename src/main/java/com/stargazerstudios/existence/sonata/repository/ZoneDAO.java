package com.stargazerstudios.existence.sonata.repository;

import com.stargazerstudios.existence.sonata.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZoneDAO extends JpaRepository<Zone, Long> {
    Optional<Zone> findByZonalPrefix(String zonalPrefix);
}
