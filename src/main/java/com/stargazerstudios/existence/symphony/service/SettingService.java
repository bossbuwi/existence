package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.wrapper.SettingWrapper;

import java.util.List;

public interface SettingService {
    List<SettingDTO> getAllSettings() throws BadJsonWebTokenException;
    SettingDTO getSettingById(long id) throws BadJsonWebTokenException, EntityNotFoundException;
    SettingDTO getSettingByKey(String key) throws EntityNotFoundException;
    List<SettingDTO> getSettingsByType(String type) throws EntityNotFoundException;
    SettingDTO modifySetting(SettingWrapper settingWrapper) throws EntityNotFoundException;
}
