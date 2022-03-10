package com.stargazerstudios.existence.symphony.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class UserDTO {

    private long id;
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private Timestamp dateAdded;
    @JsonInclude(Include.NON_EMPTY)
    private List<String> roles;
    @JsonInclude(Include.NON_EMPTY)
    private String token;
}
