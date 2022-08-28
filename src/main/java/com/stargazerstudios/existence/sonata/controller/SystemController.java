package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.validation.groups.PostFullValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.service.SystemServiceImpl;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sonata")
public class SystemController {

    @Autowired
    private SystemServiceImpl systemService;

    /* Unguarded Endpoints */
    @GetMapping("/con/systems/index")
    public ResponseEntity<List<SystemDTO>> getAllSystems() {
        return new ResponseEntity<>(systemService.getSystems(), HttpStatus.OK);
    }

    @GetMapping("/con/systems/count")
    public ResponseEntity<Long> getSystemCount() {
        return new ResponseEntity<>(systemService.countSystems(), HttpStatus.OK);
    }

    /* Guarded Endpoints */
    @PostMapping("/systems/system")
    public ResponseEntity<SystemDTO> createSystem(@Validated(PostValidation.class)
                                                      @RequestBody SystemWrapper wSystem)
            throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException {
        return new ResponseEntity<>(systemService.createSystem(wSystem), HttpStatus.OK);
    }

    @PutMapping("/systems/system")
    public ResponseEntity<SystemDTO> updateSystem(@Validated(PutValidation.class)
                                                      @RequestBody SystemWrapper wSystem)
            throws DatabaseErrorException, EntityErrorException, AuthorizationErrorException {
        return new ResponseEntity<>(systemService.updateSystem(wSystem), HttpStatus.OK);
    }

    @DeleteMapping("/systems/system/{id}")
    public ResponseEntity<SystemDTO> deleteSystem(@NotBlank @PathVariable("id") long id)
            throws DatabaseErrorException, EntityErrorException, AuthorizationErrorException {
        return new ResponseEntity<SystemDTO>(systemService.deleteSystem(id), HttpStatus.OK);
    }

    @PostMapping("/systems/system/full")
    public ResponseEntity<SystemDTO> createFullSystem(@Validated(PostFullValidation.class)
                                                      @RequestBody SystemWrapper wSystem)
            throws AuthorizationErrorException, DatabaseErrorException, EntityErrorException, UnknownInputException {
        return new ResponseEntity<SystemDTO>(systemService.createFullSystem(wSystem), HttpStatus.OK);
    }
}
