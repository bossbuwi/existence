package com.stargazerstudios.existence.requiem.controller;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.requiem.dto.CoblogDTO;
import com.stargazerstudios.existence.requiem.service.CoblogServiceImpl;
import com.stargazerstudios.existence.requiem.wrapper.CoblogWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/requiem")
public class CoblogController {

    @Autowired
    private CoblogServiceImpl coblogService;

    /** Unguarded Endpoints **/

    @GetMapping("/con/coblogs/index")
    public ResponseEntity<List<CoblogDTO>> getAllCoblogs() {
        return new ResponseEntity<>(coblogService.findAllCoblogs(), HttpStatus.OK);
    }

    /** Guarded Endpoints **/

    @PostMapping("/coblogs/coblog")
    public ResponseEntity<CoblogDTO> createCoblog(@RequestBody CoblogWrapper wrapper)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(coblogService.createCoblog(wrapper), HttpStatus.OK);
    }
}
