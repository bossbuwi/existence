package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.sonata.dto.ReleaseDTO;
import com.stargazerstudios.existence.sonata.service.ReleaseServiceImpl;
import com.stargazerstudios.existence.sonata.wrapper.ReleaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sonata")
public class ReleaseController {

    @Autowired
    private ReleaseServiceImpl releaseService;

    /* Unguarded Endpoints */
    @GetMapping("/con/releases/index")
    public ResponseEntity<List<ReleaseDTO>> getAllReleases() {
        return new ResponseEntity<>(releaseService.getReleases(), HttpStatus.OK);
    }

    /* Guarded Endpoints */
    @PostMapping("/releases/release")
    public ResponseEntity<ReleaseDTO> createRelease(@Validated(PostValidation.class)
                                                        @RequestBody ReleaseWrapper release)
            throws AuthorizationErrorException, DatabaseErrorException {
        return new ResponseEntity<>(releaseService.createRelease(release), HttpStatus.OK);
    }
}
