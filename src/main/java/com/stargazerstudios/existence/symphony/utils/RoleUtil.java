package com.stargazerstudios.existence.symphony.utils;

import com.stargazerstudios.existence.symphony.dto.RoleDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class RoleUtil {

    public RoleDTO wrapRole(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setRank(role.getRank());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setDateAdded(role.getDateAdded());

        Set<User> users = role.getUsers();
        if (!users.isEmpty()) {
            List<User> inUserList = new ArrayList<>(users);
            inUserList.sort(Comparator.comparing(User::getId));
            List<String> userList = new ArrayList<>();
            for (User user: inUserList) {
                userList.add(user.getUsername());
            }
            roleDTO.setUsers(userList);
        }

        return roleDTO;
    }
}
