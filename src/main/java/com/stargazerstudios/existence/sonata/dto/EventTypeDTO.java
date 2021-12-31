package com.stargazerstudios.existence.sonata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;

@Getter @Setter @NoArgsConstructor
public class EventTypeDTO {
    private long id;
    private String code;
    private String name;
    private String description;
    private boolean exclusive;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Timestamp creation_date;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Timestamp last_changed_date;
}
