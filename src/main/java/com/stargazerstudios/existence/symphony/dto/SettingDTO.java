package com.stargazerstudios.existence.symphony.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp last_changed_date;
}
