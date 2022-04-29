package com.stargazerstudios.existence.sonata.repository;

import com.stargazerstudios.existence.sonata.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventDAO extends JpaRepository<Event, Long> {
    @Query(value = "SELECT e FROM Event e WHERE e.startDate <= ?1 AND e.endDate >= ?1")
    List<Event> findEventsByDate(LocalDate date);
    Optional<Event> findFirstByOrderByDateCreatedDesc();
}
