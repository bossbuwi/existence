package com.stargazerstudios.existence.conductor.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.FatalErrorException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidPropertyErrorException;
import com.stargazerstudios.existence.conductor.erratum.universal.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.model.ExistenceIdentity;
import com.stargazerstudios.existence.conductor.service.ExistenceService;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;
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
    public ResponseEntity<ExistenceIdentity> announceExistence() throws FatalErrorException {
        return new ResponseEntity<>(existenceService.realizeDreams(), HttpStatus.OK);
    }

    @PostMapping("/restart")
    public ResponseEntity<UserDTO> resetAdminPassword(@RequestBody UserWrapper user)
            throws FatalErrorException, UserUnauthorizedException, InvalidPropertyErrorException {
        return new ResponseEntity<>(existenceService.startOver(user), HttpStatus.OK);
    }
}
