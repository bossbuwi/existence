package com.stargazerstudios.existence.sonata.repository;

import com.stargazerstudios.existence.sonata.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleDAO extends JpaRepository<Rule, Long> {
}
