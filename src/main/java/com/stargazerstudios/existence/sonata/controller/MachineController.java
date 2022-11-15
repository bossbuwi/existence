package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import com.stargazerstudios.existence.sonata.dto.MachineDTO;
import com.stargazerstudios.existence.sonata.service.MachineServiceImpl;
import com.stargazerstudios.existence.sonata.wrapper.MachineWrapper;
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
public class MachineController {

    @Autowired
    private MachineServiceImpl machineService;

    /* Unguarded Endpoints */
    @GetMapping("/con/machines/index")
    public ResponseEntity<List<MachineDTO>> getAllMachines() {
        return new ResponseEntity<>(machineService.getAllMachines(), HttpStatus.OK);
    }

    @GetMapping("/con/machines/count")
    public ResponseEntity<Long> getMachineCount() {
        return new ResponseEntity<>(machineService.getMachineCount(), HttpStatus.OK);
    }

    /* Guarded Endpoints */
    @PostMapping("/machines/machine")
    public ResponseEntity<MachineDTO> createMachine(@Validated(PostValidation.class)
                                                        @RequestBody MachineWrapper wMachine)
            throws DatabaseException {
        return new ResponseEntity<>(machineService.createMachine(wMachine), HttpStatus.OK);
    }

    @PutMapping("/machines/machine")
    public ResponseEntity<MachineDTO> updateMachine(@Validated(PutValidation.class)
                                                        @RequestBody MachineWrapper wMachine)
            throws DatabaseException, UnknownInputException, EntityException {
        return new ResponseEntity<>(machineService.updateMachine(wMachine), HttpStatus.OK);
    }

    @DeleteMapping("/machines/machine/{id}")
    public ResponseEntity<MachineDTO> deleteMachine(@NotBlank @PathVariable("id") long id)
            throws AuthorizationException, UnknownInputException, EntityException, DatabaseException {
        return new ResponseEntity<>(machineService.deleteMachine(id), HttpStatus.OK);
    }
}
