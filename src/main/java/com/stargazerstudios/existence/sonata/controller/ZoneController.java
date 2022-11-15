package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
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
            throws AuthorizationException, DatabaseException, UnknownInputException, EntityException {
        return new ResponseEntity<>(zoneService.createZone(wZone), HttpStatus.OK);
    }

    public ResponseEntity<ZoneDTO> updateZone() {
        return null;
    }

    public ResponseEntity<ZoneDTO> deleteZone() {
        return null;
    }
}
