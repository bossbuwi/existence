package com.stargazerstudios.existence.conductor.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.FatalErrorException;
import com.stargazerstudios.existence.conductor.model.ExistenceIdentity;
import com.stargazerstudios.existence.conductor.service.ExistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
