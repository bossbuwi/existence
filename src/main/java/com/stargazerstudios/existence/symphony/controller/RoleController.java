package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.symphony.dto.RoleDTO;
import com.stargazerstudios.existence.symphony.service.RoleServiceImpl;
import com.stargazerstudios.existence.symphony.wrapper.RoleWrapper;
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

    @GetMapping("/role")
    public ResponseEntity<RoleDTO> getRole(@RequestBody RoleWrapper role)
            throws EntityNotFoundException {
        return new ResponseEntity<>(roleService.getRole(role), HttpStatus.OK);
    }
}
