package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
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

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sonata/machines")
public class MachineController {

    @Autowired
    private MachineServiceImpl machineService;

    @GetMapping("/index")
    public ResponseEntity<List<MachineDTO>> getAllMachines() {
        return new ResponseEntity<>(machineService.getAllMachines(), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getMachineCount() {
        return new ResponseEntity<>(machineService.getMachineCount(), HttpStatus.OK);
    }

    @PostMapping("/machine")
    public ResponseEntity<MachineDTO> createMachine(@Validated(PostValidation.class)
                                                        @RequestBody MachineWrapper wMachine)
            throws DatabaseErrorException, UnknownInputException {
        return new ResponseEntity<>(machineService.createMachine(wMachine), HttpStatus.OK);
    }

    @PutMapping("/machine")
    public ResponseEntity<MachineDTO> updateMachine(@Validated(PutValidation.class)
                                                        @RequestBody MachineWrapper wMachine)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(machineService.updateMachine(wMachine), HttpStatus.OK);
    }
}
