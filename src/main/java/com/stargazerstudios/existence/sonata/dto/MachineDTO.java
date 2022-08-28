package com.stargazerstudios.existence.sonata.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;

@Getter @Setter @NoArgsConstructor
public class MachineDTO {

    private long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp creation_date;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp last_changed_date;
    private int system_count;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ArrayList<String> systems;
}
