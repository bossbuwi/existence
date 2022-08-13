package com.stargazerstudios.existence.requiem.controller;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.requiem.dto.ComponentDTO;
import com.stargazerstudios.existence.requiem.service.ComponentServiceImpl;
import com.stargazerstudios.existence.requiem.wrapper.ComponentWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/requiem")
public class ComponentController {

    @Autowired
    private ComponentServiceImpl componentService;

    /** Unguarded Endpoints **/

    @GetMapping("/con/components/index")
    public ResponseEntity<List<ComponentDTO>> getAllComponents() {
        return new ResponseEntity<>(componentService.getAllComponents(), HttpStatus.OK);
    }

    /** Guarded Endpoints **/

    @PostMapping("/components/component")
    public ResponseEntity<ComponentDTO> createComponent(@RequestBody ComponentWrapper wrapper)
            throws EntityErrorException, DatabaseErrorException {
        return new ResponseEntity<>(componentService.createComponent(wrapper), HttpStatus.OK);
    }
}
