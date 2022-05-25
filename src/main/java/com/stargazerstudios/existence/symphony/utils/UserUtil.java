package com.stargazerstudios.existence.symphony.utils;

import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class UserUtil {

    public UserDTO wrapUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setToken(user.getToken());

        Set<Role> roles = user.getRoles();

        if (!roles.isEmpty()) {
            List<Role> roleList = new ArrayList<>(roles);
            roleList.sort(Comparator.comparing(Role::getId));
            List<String> roleNames = new ArrayList<>();
            for (Role role: roleList) {
                roleNames.add(role.getName());
            }
            userDTO.setRoles(roleNames);
        }
        return userDTO;
    }
}
