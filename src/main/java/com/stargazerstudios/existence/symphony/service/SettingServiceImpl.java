package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemException;
import com.stargazerstudios.existence.conductor.erratum.system.InvalidSettingException;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
import com.stargazerstudios.existence.symphony.constants.EnumSettingType;
import com.stargazerstudios.existence.symphony.constants.EnumValidValues;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.repository.SettingDAO;
import com.stargazerstudios.existence.symphony.entity.Setting;
import com.stargazerstudios.existence.symphony.utils.SettingUtil;
import com.stargazerstudios.existence.symphony.wrapper.SettingWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
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
    public SettingDTO getSettingById(long id) throws EntityException {
        Optional<Setting> settingData = settingDAO.findById(id);
        if (settingData.isPresent()) {
            return settingUtil.wrapSetting(settingData.get());
        } else {
            throw new EntityNotFoundException("setting", "id", Long.toString(id));
        }
    }

    @Override
    public SettingDTO getSettingByKey(String key) throws EntityException {
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
            throws EntityException, DatabaseException, AuthorizationException, SystemException {
        boolean isSuperuserOrHigher = authorityUtil.checkAuthority(EnumAuthorization.SUPERUSER.getValue());
        if (!isSuperuserOrHigher) throw new UserUnauthorizedException();
        String username = authorityUtil.getAuthUsername();

        String key = wSetting.getKey();
        String newValue = wSetting.getValue();
        Optional<Setting> settingData = settingDAO.findSettingByKey(key);
        if (settingData.isEmpty()) throw new EntityNotFoundException("setting", "key", key);

        Setting setting = settingData.get();
        String validValuesStr = setting.getValidValues().trim();
        long validLength = setting.getLength();

        // Validation sequence
        // 1. Check if length is acceptable
        if (newValue.length() > validLength) {
            throw new InvalidSettingException(setting.getKey());
        } else {
            // 2. Check if valid values are specific or generic
            // Specific valid values are required to be delimited by commas
            int index = validValuesStr.indexOf(",");
            if (index == -1) {
                if (validValuesStr.equals(EnumValidValues.ALPHA.getValue())) {
                    // Valid value of type alpha
                    boolean allLetters = newValue.chars().allMatch(Character::isLetter);
                    if (!allLetters) throw new InvalidSettingException(setting.getKey());
                } else if (validValuesStr.equals(EnumValidValues.NUMERIC.getValue())) {
                    // Valid value of type numeric
                    boolean allNumbers = StringUtils.isNumeric(newValue);
                    if (!allNumbers) throw new InvalidSettingException(setting.getKey());
                }
                // If not alpha or numeric, it means it is alphanumeric
                // Thus it will not enter any of the if clauses
                // This however accepts every known character without any validation
                // If there are only specific characters to be accepted, a new validation must be added

                setting.setValue(newValue);
                setting.setChangedBy(username);
            } else {
                List<String> validValues = Arrays.asList(validValuesStr.split(","));
                if (validValues.contains(newValue) && newValue.length() == setting.getLength()) {
                    setting.setValue(newValue);
                    setting.setChangedBy(username);
                } else {
                    throw new InvalidSettingException(setting.getKey());
                }
            }
        }

        try {
            settingDAO.saveAndFlush(setting);
        } catch (Exception e) {
            throw new EntitySaveException("setting");
        }

        return settingUtil.wrapSetting(setting);
    }
}
