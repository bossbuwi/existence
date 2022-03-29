package com.stargazerstudios.existence.sonata.repository;

import com.stargazerstudios.existence.sonata.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZoneDAO extends JpaRepository<Zone, Long> {
    @Query("SELECT z FROM Zone s WHERE z.zonalPrefix = :zonalPrefix and z.system.globalPrefix = :globalPrefix")
    Optional<Zone> findZoneOnSystem(@Param("zonalPrefix") String zonalPrefix, @Param("globalPrefix") String globalPrefix);
}
