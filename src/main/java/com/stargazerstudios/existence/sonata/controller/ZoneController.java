package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.service.ZoneServiceImpl;
import com.stargazerstudios.existence.sonata.wrapper.ZoneWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sonata/zones")
public class ZoneController {

    @Autowired
    private ZoneServiceImpl zoneService;

    @GetMapping("/index")
    public ResponseEntity<List<ZoneDTO>> getAllZones() {
        return new ResponseEntity<>(zoneService.getAllZones(), HttpStatus.OK);
    }

    @PostMapping("/zone")
    public ResponseEntity<ZoneDTO> createZone(@RequestBody ZoneWrapper wZone)
            throws InvalidInputException, DuplicateEntityException, EntityNotFoundException {
        return new ResponseEntity<>(zoneService.createZone(wZone), HttpStatus.OK);
    }
}
