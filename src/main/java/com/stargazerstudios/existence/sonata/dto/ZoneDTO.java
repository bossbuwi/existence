package com.stargazerstudios.existence.sonata.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;

@Getter @Setter @NoArgsConstructor
public class ZoneDTO {

    private long id;
    private String zonal_prefix;
    private String zone_name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String machine;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String system;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Timestamp creation_date;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Timestamp last_changed_date;
}
