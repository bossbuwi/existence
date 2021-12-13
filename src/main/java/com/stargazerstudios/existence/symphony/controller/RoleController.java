package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.BadJsonWebTokenException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.symphony.dto.RoleDTO;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.entity.Role;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.RoleDAO;
import com.stargazerstudios.existence.symphony.service.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/symphony/roles")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping("/index")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/users/{rolename}")
    public ResponseEntity<RoleDTO> getAllUsersWithRole(@PathVariable("rolename") String roleName)
            throws EntityNotFoundException {
        return new ResponseEntity<>(roleService.getAllUsersWithRole(roleName), HttpStatus.OK);
    }
}
