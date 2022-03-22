package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.symphony.dto.RoleDTO;
import com.stargazerstudios.existence.symphony.wrapper.RoleWrapper;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAllRoles();
    RoleDTO getRole(RoleWrapper role) throws EntityNotFoundException;
}
