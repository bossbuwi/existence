package com.stargazerstudios.existence.symphony.dto;

import com.stargazerstudios.existence.symphony.entity.Setting;
import lombok.*;

import java.sql.Timestamp;

@Getter @Setter @NoArgsConstructor
public class SettingDTO {

    private long id;
    private String key;
    private String value;
    private long length;
    private String type;
    private String desc;
    private String default_value;
    private String valid_values;
    private String added_by;
    private String last_changed_by;
    private Timestamp last_changed_date;

    public SettingDTO(Setting setting) {
        setId(setting.getId());
        setKey(setting.getKey());
        setValue(setting.getValue());
        setLength(setting.getLength());
        setType(setting.getType());
        setDesc(setting.getDescription());
        setDefault_value(setting.getDefaultValue());
        setValid_values(setting.getValidValues());
        setAdded_by(setting.getAddedBy());
        setLast_changed_by(setting.getChangedBy());
        setLast_changed_date(setting.getDateChanged());
    }
}
