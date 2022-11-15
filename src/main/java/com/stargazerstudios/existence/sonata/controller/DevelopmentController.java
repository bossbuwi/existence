package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.sonata.service.DevelopmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/sonata")
public class DevelopmentController {

    @Autowired
    private DevelopmentService developmentService;

    @DeleteMapping("/dev/delete-all/events")
    public ResponseEntity<Boolean> deleteAllEvents() throws AuthorizationException {
        return new ResponseEntity<>(developmentService.deleteAllEvents(), HttpStatus.OK);
    }
}
