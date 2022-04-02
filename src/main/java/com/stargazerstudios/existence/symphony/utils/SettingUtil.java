package com.stargazerstudios.existence.symphony.utils;

import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.entity.Setting;
import com.stargazerstudios.existence.symphony.repository.SettingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingUtil {

    @Autowired
    private SettingDAO settingDAO;

    public SettingDTO wrapSetting(Setting setting) {
        SettingDTO settingDTO = new SettingDTO();
        settingDTO.setId(setting.getId());
        settingDTO.setKey(setting.getKey());
        settingDTO.setValue(setting.getValue());
        settingDTO.setLength(setting.getLength());
        settingDTO.setType(setting.getType());
        settingDTO.setDesc(setting.getDescription());
        settingDTO.setDefault_value(setting.getDefaultValue());
        settingDTO.setValid_values(setting.getValidValues());
        settingDTO.setAdded_by(setting.getAddedBy());
        settingDTO.setLast_changed_by(setting.getChangedBy());
        settingDTO.setLast_changed_date(setting.getDateChanged());

        return settingDTO;
    }

    public String getCurrentSetting(String key) {
        Optional<Setting> settingData = settingDAO.findSettingByKey(key);
        if (settingData.isEmpty()) return EnumUtilOutput.INVALID.getValue();
        return settingData.get().getValue();
    }
}
