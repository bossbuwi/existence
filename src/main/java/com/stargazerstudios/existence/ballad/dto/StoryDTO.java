package com.stargazerstudios.existence.ballad.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;

@Getter @Setter @NoArgsConstructor
public class StoryDTO {
    private long id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ArrayList<String> tags;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp creation_date;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Timestamp last_changed_date;

}
