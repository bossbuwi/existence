package com.stargazerstudios.existence.sonata.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter @NoArgsConstructor
public class SystemDTO {

    private long id;
    private String global_prefix;
    private String release;
    private String description;
    private String url;
    private String owners;
    private String machine;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ArrayList<ZoneDTO> zones;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ArrayList<String> zone_names;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ArrayList<String> zone_prefixes;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp creation_date;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp last_changed_date;
}
