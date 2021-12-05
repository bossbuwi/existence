package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.symphony.dto.RoleDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleDAO.findAll();
        List<RoleDTO> roleDTOList = new ArrayList<>();
        for (Role role: roles) {
            RoleDTO roleDTO = new RoleDTO(role);
            roleDTOList.add(roleDTO);
        }

        return roleDTOList;
    }

    @Override
    public RoleDTO getAllUsersWithRole(String roleName) throws EntityNotFoundException {
        Optional<Role> roleData = roleDAO.findByName(roleName);
        if (roleData.isPresent()) {
            Role role = roleData.get();
            RoleDTO roleDTO = new RoleDTO(role, true);
            return roleDTO;
        } else {
            throw new EntityNotFoundException("Role with name: " + roleName + " not found.");
        }
    }
}
