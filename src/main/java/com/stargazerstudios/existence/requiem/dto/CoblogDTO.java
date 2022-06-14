package com.stargazerstudios.existence.requiem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;

@Getter @Setter @NoArgsConstructor
public class CoblogDTO {

    private long id;
    private String description;
    private String run_day;
    private String next_run_day;
    private boolean open;
    private String active_user;
    private String conclusion;
    private long number_of_components;
    private String created_by;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp creation_date;
    private String last_changed_by;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp last_changed_date;
}
