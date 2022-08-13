package com.stargazerstudios.existence.requiem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;

@Getter @Setter @NoArgsConstructor
public class ComponentDTO {
    private long id;
    private String name;
    private String sequence;
    private String details;
    private String resolution;
    private String created_by;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp creation_date;
    private String last_changed_by;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp last_changed_date;
}
