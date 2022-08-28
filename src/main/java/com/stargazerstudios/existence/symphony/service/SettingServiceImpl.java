package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemErrorException;
import com.stargazerstudios.existence.conductor.erratum.system.InvalidSettingException;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
import com.stargazerstudios.existence.symphony.constants.EnumSettingType;
import com.stargazerstudios.existence.symphony.constants.EnumValidValues;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.repository.SettingDAO;
import com.stargazerstudios.existence.symphony.entity.Setting;
import com.stargazerstudios.existence.symphony.utils.SettingUtil;
import com.stargazerstudios.existence.symphony.wrapper.SettingWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SettingServiceImpl implements SettingService{

    @Autowired
    private SettingDAO settingDAO;

    @Autowired
    private SettingUtil settingUtil;

    @Autowired
    private AuthorityUtil authorityUtil;
    
    @Override
    public List<SettingDTO> getAllSettings() {
        List<Setting> settingList = settingDAO.findAll();
        List<SettingDTO> settingDTOList = new ArrayList<>();
        if (!settingList.isEmpty()) {
            for (Setting setting : settingList) {
                settingDTOList.add(settingUtil.wrapSetting(setting));
            }
        }
        return settingDTOList;
    }

    @Override
    public List<SettingDTO> getAllSwitchableFeatures() {
        List<Setting> settingList = settingDAO.findSettingByType(EnumSettingType.SWITCHABLE_FEATURE.getValue());
        List<SettingDTO> settingDTOList = new ArrayList<>();
        if (!settingList.isEmpty()) {
            for (Setting setting : settingList) {
                settingDTOList.add(settingUtil.wrapSetting(setting));
            }
        }
        return settingDTOList;
    }

    @Override
    public List<SettingDTO> getAllBackendFeatures() {
        List<Setting> settingList = settingDAO.findSettingByType(EnumSettingType.BACKEND_FEATURE.getValue());
        List<SettingDTO> settingDTOList = new ArrayList<>();
        if (!settingList.isEmpty()) {
            for (Setting setting : settingList) {
                settingDTOList.add(settingUtil.wrapSetting(setting));
            }
        }
        return settingDTOList;
    }

    @Override
    public List<SettingDTO> getAllFrontendFeatures() {
        List<Setting> settingList = settingDAO.findSettingByType(EnumSettingType.FRONTEND_FEATURE.getValue());
        List<SettingDTO> settingDTOList = new ArrayList<>();
        if (!settingList.isEmpty()) {
            for (Setting setting : settingList) {
                settingDTOList.add(settingUtil.wrapSetting(setting));
            }
        }
        return settingDTOList;
    }

    @Override
    public List<SettingDTO> getDisabledSwitchableFeatures() {
        List<SettingDTO> disabledSwitchableList = getAllSwitchableFeatures();
        disabledSwitchableList.removeIf(x -> x.getValue().equals("Y"));
        return disabledSwitchableList;
    }

    @Override
    public SettingDTO getSettingById(long id) throws EntityErrorException {
        Optional<Setting> settingData = settingDAO.findById(id);
        if (settingData.isPresent()) {
            return settingUtil.wrapSetting(settingData.get());
        } else {
            throw new EntityNotFoundException("setting", "id", Long.toString(id));
        }
    }

    @Override
    public SettingDTO getSettingByKey(String key) throws EntityErrorException {
        Optional<Setting> settingData = settingDAO.findSettingByKey(key);
        if (settingData.isPresent()) {
            Setting setting = settingData.get();
            return settingUtil.wrapSetting(setting);
        } else {
            throw new EntityNotFoundException("setting", "key", key);
        }
    }

    @Override
    public SettingDTO modifySetting(SettingWrapper wSetting)
            throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException, SystemErrorException {
        boolean isSuperuserOrHigher = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isSuperuserOrHigher) throw new UserUnauthorizedException();
        String username = authorityUtil.getAuthUsername();

        String key = wSetting.getKey();
        String newValue = wSetting.getValue();
        Optional<Setting> settingData = settingDAO.findSettingByKey(key);
        if (settingData.isEmpty()) throw new EntityNotFoundException("setting", "key", key);

        Setting setting = settingData.get();
        String validValuesStr = setting.getValidValues();
        int index = validValuesStr.indexOf(",");
        if (index == -1) {
            // length must be checked first if valid
            if (validValuesStr.equals(EnumValidValues.ALPHA.getValue())) {
                //check for valid alpha
            } else if (validValuesStr.equals(EnumValidValues.NUMERIC.getValue())) {
                //check for valid number
            } else if (validValuesStr.equals(EnumValidValues.ALPHANUMERIC.getValue())) {
                //basically anything is possible here
                //is it still needed?
            }
            setting.setValue(newValue);
            setting.setChangedBy(username);
        } else {
            List<String> validValues = Arrays.asList(validValuesStr.split(","));
            if (validValues.contains(newValue) && newValue.length() == setting.getLength()) {
                setting.setValue(newValue);
                setting.setChangedBy(username);
            } else {
                if (settingUtil.getCurrentSetting("settingexpliciterror").equals("Y"))
                    throw new InvalidSettingException(setting.getKey());

                setting.setValue(setting.getDefaultValue());
                setting.setChangedBy(username);
            }
        }

        try {
            settingDAO.save(setting);
        } catch (Exception e) {
            throw new EntitySaveErrorException("setting");
        }

        return settingUtil.wrapSetting(setting);
    }
}
