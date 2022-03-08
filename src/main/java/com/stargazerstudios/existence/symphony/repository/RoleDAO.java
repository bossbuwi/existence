package com.stargazerstudios.existence.symphony.repository;

import com.stargazerstudios.existence.symphony.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    @Query("SELECT r1 FROM Role r1 WHERE r1.name IN :roles")
    List<Role> findRolesBySet(@Param("roles")Set<String> roles);
}
