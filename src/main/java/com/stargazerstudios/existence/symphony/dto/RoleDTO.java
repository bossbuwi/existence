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
public class RoleDTO {

    private long id;
    private String name;
    @JsonInclude(Include.NON_NULL)
    private String description;
    @JsonIgnore
    private long rank;
    @JsonIgnore
    private Timestamp dateAdded;
//    @JsonInclude(Include.NON_EMPTY)
//    private List<UserDTO> users = new ArrayList<>();
    @JsonInclude(Include.NON_EMPTY)
    private List<String> users;


//    public RoleDTO(Role role) {
//        setId(role.getId());
//        setName(role.getName());
//        setDescription(role.getDescription());
//    }

//    public RoleDTO(Role role, boolean withUsers) {
//        setId(role.getId());
//        setName(role.getName());
//        setDescription(role.getDescription());
//        for (User user: role.getUsers()) {
//            UserDTO userDTO = new UserDTO();
//            userDTO.setUsername(user.getUsername());
//            users.add(userDTO);
//        }
//    }
}
