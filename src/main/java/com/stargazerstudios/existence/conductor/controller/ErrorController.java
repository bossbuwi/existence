package com.stargazerstudios.existence.conductor.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.GenericErrorException;
import com.stargazerstudios.existence.conductor.model.ExistenceIdentity;
import com.stargazerstudios.existence.conductor.service.FrontendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/conductor/erratum")
public class ErrorController {
    // TODO: Delete this class after use.
    //  This is just a test endpoint that always output an error.

    @Autowired
    private FrontendService frontendService;

    @GetMapping("/get")
    public ResponseEntity<ExistenceIdentity> getError() throws GenericErrorException {
        int number = 1;
        if (number == 1) throw new GenericErrorException("This is a test GET endpoint.");
        return new ResponseEntity<>(frontendService.getAppIdentity(), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<ExistenceIdentity> postError() throws GenericErrorException {
        int number = 1;
        if (number == 1) throw new GenericErrorException("This is a test POST endpoint.");
        return new ResponseEntity<>(frontendService.getAppIdentity(), HttpStatus.OK);
    }

    @PutMapping("/put")
    public ResponseEntity<ExistenceIdentity> putError() throws GenericErrorException {
        int number = 1;
        if (number == 1) throw new GenericErrorException("This is a test PUT endpoint.");
        return new ResponseEntity<>(frontendService.getAppIdentity(), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ExistenceIdentity> deleteError() throws GenericErrorException {
        int number = 1;
        if (number == 1) throw new GenericErrorException("This is a test DELETE endpoint.");
        return new ResponseEntity<>(frontendService.getAppIdentity(), HttpStatus.OK);
    }
}
