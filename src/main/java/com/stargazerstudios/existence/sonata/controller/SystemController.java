package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.service.SystemServiceImpl;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    @PostMapping("/system")
    public ResponseEntity<SystemDTO> createSystem(@RequestBody SystemWrapper wSystem)
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        return new ResponseEntity<>(systemService.createSystem(wSystem), HttpStatus.OK);
    }
}
