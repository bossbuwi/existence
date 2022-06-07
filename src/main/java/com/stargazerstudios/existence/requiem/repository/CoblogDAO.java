package com.stargazerstudios.existence.requiem.repository;

import com.stargazerstudios.existence.requiem.entity.Coblog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoblogDAO extends JpaRepository<Coblog, Long> {
}
