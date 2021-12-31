package com.stargazerstudios.existence.sonata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Getter @Setter @NoArgsConstructor
public class EventDTO {
    private long id;
    private String system;
    private ArrayList<String> zones;
    private ArrayList<String> event_types;
    private Date start_date;
    private Date end_date;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String jira_case;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String features_on;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String features_off;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String compiled_sources;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String api_used;
    private String created_by;
    private Timestamp creation_date;
    private String last_changed_by;
    private Timestamp last_changed_date;
}
