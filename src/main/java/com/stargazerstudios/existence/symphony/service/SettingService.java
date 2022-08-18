package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemErrorException;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.wrapper.SettingWrapper;

import java.util.List;

public interface SettingService {
    List<SettingDTO> getAllSettings();
    List<SettingDTO> getAllSwitchableFeatures();
    List<SettingDTO> getAllBackendFeatures();
    List<SettingDTO> getAllFrontendFeatures();
    List<SettingDTO> getDisabledSwitchableFeatures();
    SettingDTO getSettingById(long id) throws EntityErrorException;
    SettingDTO getSettingByKey(String key) throws EntityErrorException;
    SettingDTO modifySetting(SettingWrapper settingWrapper) throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException, SystemErrorException;
}
