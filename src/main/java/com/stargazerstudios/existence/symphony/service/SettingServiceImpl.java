package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.repository.SettingDAO;
import com.stargazerstudios.existence.symphony.entity.Setting;
import com.stargazerstudios.existence.symphony.wrapper.SettingWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class SettingServiceImpl implements SettingService{

    @Autowired
    SettingDAO settingDAO;
    
    @Override
    public List<SettingDTO> getAllSettings() {
        List<Setting> settingList = settingDAO.findAll();
        List<SettingDTO> settingDTOList = new ArrayList<>();
        if (!settingList.isEmpty()) {
            for (Setting setting : settingList) {
                SettingDTO settingDTO = new SettingDTO(setting);
                settingDTOList.add(settingDTO);
            }
        }
        return settingDTOList;
    }

    @Override
    public SettingDTO getSettingById(long id) throws EntityNotFoundException {
        Optional<Setting> settingData = settingDAO.findById(id);
        if (settingData.isPresent()) {
            return new SettingDTO(settingData.get());
        } else {
            throw new EntityNotFoundException("setting", "id", Long.toString(id));
        }
    }

    @Override
    public SettingDTO getSettingByKey(String key)
            throws EntityNotFoundException {
        Optional<Setting> settingData = settingDAO.findSettingByKey(key);
        if (settingData.isPresent()) {
            Setting setting = settingData.get();
            return new SettingDTO(setting);
        } else {
            throw new EntityNotFoundException("setting", "key", key);
        }
    }

    @Override
    public List<SettingDTO> getSettingsByType(String type) throws EntityNotFoundException {
        List<SettingDTO> settingDtoList = new ArrayList<>();
        List<Setting> settingList = settingDAO.findSettingByType(type);
        if (!settingList.isEmpty()) {
            for (Setting setting: settingList) {
                SettingDTO settingDTO = new SettingDTO(setting);
                settingDtoList.add(settingDTO);
            }
        } else {
            throw new EntityNotFoundException("setting", "type", type);
        }
        return settingDtoList;
    }

    @Override
    public SettingDTO modifySetting(SettingWrapper wSetting) throws EntityNotFoundException  {
        HashMap<String, String> parsedJSON = wSetting.getSetting();
        String key = parsedJSON.get("key");
        Optional<Setting> settingData = settingDAO.findSettingByKey(key);
        if (settingData.isPresent()) {
            Setting setting = settingData.get();
            setting.setValue(parsedJSON.get("value"));
            setting.setChangedBy(parsedJSON.get("last_changed_by"));
            return new SettingDTO(settingDAO.save(setting));
        } else {
            throw new EntityNotFoundException("setting", "key", key);
        }
    }
}
