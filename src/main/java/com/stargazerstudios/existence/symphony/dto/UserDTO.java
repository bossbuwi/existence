package com.stargazerstudios.existence.symphony.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
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
//    private List<RoleDTO> roles = new ArrayList<>();
//    @JsonInclude(Include.NON_EMPTY)
    private List<String> roles;
    @JsonInclude(Include.NON_EMPTY)
    private String token;

//    public UserDTO(User user) {
//        setId(user.getId());
//        setUsername(user.getUsername());
//        setToken(user.getToken());
//        for (Role role: user.getRoles()) {
//            RoleDTO roleDTO = new RoleDTO();
//            roleDTO.setId(role.getId());
//            roleDTO.setName(role.getName());
//            roles.add(roleDTO);
//        }
//    }
}
