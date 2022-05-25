package com.stargazerstudios.existence.conductor.controller;

import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemErrorException;
import com.stargazerstudios.existence.conductor.model.ExistenceIdentity;
import com.stargazerstudios.existence.conductor.service.ExistenceService;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.AuthWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/drapes/")
public class ExistenceController {

    @Autowired
    private ExistenceService existenceService;

    @GetMapping("/dreams")
    public ResponseEntity<ExistenceIdentity> announceExistence() {
        return new ResponseEntity<>(existenceService.instigate(), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<UserDTO> resetAdminPassword(@RequestBody AuthWrapper user)
            throws SystemErrorException, UserUnauthorizedException {
        return new ResponseEntity<>(existenceService.resetAdminPassword(user), HttpStatus.OK);
    }

    @PostMapping("/reset-roles")
    public ResponseEntity<UserDTO> resetAdminRoles(@RequestBody AuthWrapper user)
            throws SystemErrorException, UserUnauthorizedException {
        return new ResponseEntity<>(existenceService.resetAdminRoles(user), HttpStatus.OK);
    }
}
