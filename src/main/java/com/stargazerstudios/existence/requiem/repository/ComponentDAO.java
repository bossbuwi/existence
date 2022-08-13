package com.stargazerstudios.existence.requiem.repository;

import com.stargazerstudios.existence.requiem.entity.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentDAO extends JpaRepository<Component, Long> {

    List<Component> findByName(String name);
}
