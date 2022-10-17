package com.stargazerstudios.existence.sonata.repository;

import com.stargazerstudios.existence.sonata.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventDAO extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    @Query(value = "SELECT e FROM Event e WHERE e.startDate <= ?1 AND e.endDate >= ?1")
    List<Event> findEventsByDate(LocalDate date);
    @Query(value = "SELECT e FROM Event e " +
            "WHERE e.startDate BETWEEN ?1 AND ?2 " +
            "OR ?1 BETWEEN e.startDate AND e.endDate")
    List<Event> findEventsBetweenDates(LocalDate dateStart, LocalDate dateEnd);
    Optional<Event> findFirstByOrderByDateCreatedDesc();
    long countByCreatedBy(String username);
    Optional<Event> findFirstByCreatedByOrderByDateCreatedDesc(String username);
    @Query(value = "SELECT * FROM sonata_events WHERE tsv @@ websearch_to_tsquery(?1)", nativeQuery = true)
    List<Event> search(String keyword);
    @Procedure("tsvector_manual_update_events")
    void generateTSVData(@Param("item_id") long id);
}
