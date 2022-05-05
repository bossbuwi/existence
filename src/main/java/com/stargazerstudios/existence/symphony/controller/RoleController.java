package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.validation.groups.GetValidation;
import com.stargazerstudios.existence.symphony.dto.RoleDTO;
import com.stargazerstudios.existence.symphony.service.RoleServiceImpl;
import com.stargazerstudios.existence.symphony.wrapper.RoleWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/symphony")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleService;

    /* Unguarded Endpoints */
    @GetMapping("/con/roles/index")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    /* Guarded Endpoints */
    @GetMapping("/roles/role")
    public ResponseEntity<RoleDTO> getRole(@Validated(GetValidation.class)
                                               @RequestBody RoleWrapper role)
            throws EntityNotFoundException {
        return new ResponseEntity<>(roleService.getRole(role), HttpStatus.OK);
    }
}
