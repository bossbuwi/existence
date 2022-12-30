package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemException;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.wrapper.SettingWrapper;

import java.util.List;

public interface SettingService {
    List<SettingDTO> getAllSettings();
    List<SettingDTO> getAllSwitchableFeatures();
    List<SettingDTO> getAllBackendFeatures();
    List<SettingDTO> getAllFrontendFeatures();
    List<SettingDTO> getDisabledSwitchableFeatures();
    SettingDTO getSettingById(long id) throws EntityException;
    SettingDTO getSettingByKey(String key) throws EntityException;
    SettingDTO modifySetting(SettingWrapper settingWrapper) throws EntityException, DatabaseException, AuthorizationException, SystemException;
}
