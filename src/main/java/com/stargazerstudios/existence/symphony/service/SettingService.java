package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.entity.Setting;

import java.util.List;
import java.util.Optional;

public interface SettingService {
    List<SettingDTO> getAllSettings() throws BadJsonWebTokenException;
    SettingDTO getSettingById(long id) throws BadJsonWebTokenException, EntityNotFoundException;
    SettingDTO getSettingByKey(String key) throws EntityNotFoundException;
    List<Setting> getSettingByType(String type) throws BadJsonWebTokenException;
    Optional<Setting> modifySetting(Setting setting)
            throws BadJsonWebTokenException, UserNotFoundException, UserUnauthorizedException;
}
