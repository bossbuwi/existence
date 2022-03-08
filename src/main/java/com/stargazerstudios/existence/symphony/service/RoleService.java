package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.symphony.dto.RoleDTO;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.wrapper.RoleWrapper;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAllRoles();
//    RoleDTO getAllUsersWithRole(String roleName) throws EntityNotFoundException;
    RoleDTO getRole(RoleWrapper role) throws EntityNotFoundException;
}
