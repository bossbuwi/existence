package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.service.ZoneServiceImpl;
import com.stargazerstudios.existence.sonata.wrapper.ZoneWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sonata")
public class ZoneController {

    @Autowired
    private ZoneServiceImpl zoneService;

    /* Unguarded Endpoints */
    @GetMapping("/con/zones/index")
    public ResponseEntity<List<ZoneDTO>> getAllZones() {
        return new ResponseEntity<>(zoneService.getAllZones(), HttpStatus.OK);
    }

    /* Guarded Endpoints */
    @PostMapping("/zones/zone")
    public ResponseEntity<ZoneDTO> createZone(@Validated(PostValidation.class)
                                                  @RequestBody ZoneWrapper wZone)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(zoneService.createZone(wZone), HttpStatus.OK);
    }
}
