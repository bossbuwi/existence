package com.stargazerstudios.existence.symphony.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class RoleDTO {

    private long id;
    private String name;
    @JsonInclude(Include.NON_NULL)
    private String description;
    @JsonIgnore
    private long rank;
    @JsonIgnore
    private Timestamp dateAdded;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> users;
}
