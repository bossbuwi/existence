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
    public List<SettingDTO> getSettingsByType(String type)  throws EntityErrorException{
        List<SettingDTO> settingDtoList = new ArrayList<>();
        List<Setting> settingList = settingDAO.findSettingByType(type);
        if (!settingList.isEmpty()) {
            for (Setting setting: settingList) {
                settingDtoList.add(settingUtil.wrapSetting(setting));
            }
        } else {
            throw new EntityNotFoundException("setting", "type", type);
        }
        return settingDtoList;
    }

    @Override
    public SettingDTO modifySetting(SettingWrapper wSetting)
            throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException, SystemErrorException {
        boolean isAdminOrHigher = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAdminOrHigher) throw new UserUnauthorizedException();
        String username = authorityUtil.getAuthUsername();

        String key = wSetting.getKey();
        String newValue = wSetting.getValue();
        Optional<Setting> settingData = settingDAO.findSettingByKey(key);
        if (settingData.isEmpty()) throw new EntityNotFoundException("setting", "key", key);

        Setting setting = settingData.get();
        String validValuesStr = setting.getValidValues();
        List<String> validValues = Arrays.asList(validValuesStr.split(","));
        if (validValues.contains(newValue) && newValue.length() == setting.getLength()) {
            setting.setValue(newValue);
            setting.setChangedBy(username);
        } else {
            if (settingUtil.getCurrentSetting("settingexpliciterror").equals("Y"))
                throw new InvalidSettingException("settingexpliciterror");

            setting.setValue(setting.getDefaultValue());
            setting.setChangedBy(username);
        }

        try {
            settingDAO.save(setting);
        } catch (Exception e) {
            throw new EntitySaveErrorException("setting");
        }

        return settingUtil.wrapSetting(setting);
    }
}
