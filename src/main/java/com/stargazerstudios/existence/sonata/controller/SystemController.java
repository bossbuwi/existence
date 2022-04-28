package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.service.SystemServiceImpl;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sonata/systems")
public class SystemController {

    @Autowired
    private SystemServiceImpl systemService;

    @GetMapping("/index")
    public ResponseEntity<List<SystemDTO>> getAllSystems() {
        return new ResponseEntity<>(systemService.getAllSystems(), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getSystemCount() {
        return new ResponseEntity<>(systemService.countSystems(), HttpStatus.OK);
    }

    @PostMapping("/system")
    public ResponseEntity<SystemDTO> createSystem(@Validated(PostValidation.class)
                                                      @RequestBody SystemWrapper wSystem)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException {
        return new ResponseEntity<>(systemService.createSystem(wSystem), HttpStatus.OK);
    }
}
