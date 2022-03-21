package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.symphony.dto.RoleDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.utils.RoleUtil;
import com.stargazerstudios.existence.symphony.wrapper.RoleWrapper;
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
    private RoleUtil roleUtil;

    @Autowired
    private StringUtil stringUtil;

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleDAO.findAll();
        List<RoleDTO> roleDTOList = new ArrayList<>();

        for (Role role: roles) {
            RoleDTO roleDTO = roleUtil.wrapRole(role);
            roleDTOList.add(roleDTO);
        }

        return roleDTOList;
    }

    @Override
    public RoleDTO getRole(RoleWrapper wRole) throws EntityNotFoundException {
        String name = stringUtil.checkInput(wRole.getName());
        Optional<Role> roleData = roleDAO.findByName(name);
        if (roleData.isPresent()) {
            Role role = roleData.get();
            return roleUtil.wrapRole(role);
        } else {
            throw new EntityNotFoundException("role", "name", name);
        }
    }
}
