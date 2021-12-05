package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.repository.SettingDAO;
import com.stargazerstudios.existence.symphony.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            throw new EntityNotFoundException("Setting with id: " + id + " not found.");
        }
    }

    @Override
    public Optional<Setting> getSettingByKey(String key) {
        return Optional.empty();
    }

    @Override
    public List<Setting> getSettingByType(String type) {
        return null;
    }

    @Override
    public Optional<Setting> modifySetting(Setting setting)
            throws UserNotFoundException, UserUnauthorizedException {
        return Optional.empty();
    }
}
