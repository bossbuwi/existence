package com.stargazerstudios.existence.symphony.repository;

import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.symphony.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingDAO extends JpaRepository<Setting, Long> {
    Optional<Setting> findSettingByKey(String key) throws EntityNotFoundException;
    List<Setting> findSettingByType(String type);
}
