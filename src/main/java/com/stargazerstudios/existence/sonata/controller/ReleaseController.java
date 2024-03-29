package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import com.stargazerstudios.existence.sonata.dto.ReleaseDTO;
import com.stargazerstudios.existence.sonata.service.ReleaseServiceImpl;
import com.stargazerstudios.existence.sonata.wrapper.ReleaseWrapper;
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
            throws AuthorizationException, DatabaseException {
        return new ResponseEntity<>(releaseService.createRelease(release), HttpStatus.OK);
    }

    // TODO: Update and delete release entity both on frontend and backend.
    @PutMapping("/releases/release")
    public ResponseEntity<ReleaseDTO> updateRelease(@Validated(PutValidation.class)
                                                    @RequestBody ReleaseWrapper release) {
        return new ResponseEntity<>(releaseService.updateRelease(release), HttpStatus.OK);
    }

    @DeleteMapping("/releases/release/{id}")
    public ResponseEntity<ReleaseDTO> deleteRelease(@NotBlank @PathVariable("id") long id) {
        return new ResponseEntity<>(releaseService.deleteRelease(id), HttpStatus.OK);
    }
}
