package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.MachineDTO;
import com.stargazerstudios.existence.sonata.service.MachineServiceImpl;
import com.stargazerstudios.existence.sonata.wrapper.MachineWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/machine")
    public ResponseEntity<MachineDTO> createMachine(@RequestBody MachineWrapper wMachine)
            throws DuplicateEntityException, InvalidInputException {
        return new ResponseEntity<>(machineService.createMachine(wMachine), HttpStatus.OK);
    }

    @PutMapping("/machine")
    public ResponseEntity<MachineDTO> updateMachine(@RequestBody MachineWrapper wMachine)
            throws EntityNotFoundException, DuplicateEntityException, InvalidInputException {
        return new ResponseEntity<>(machineService.updateMachine(wMachine), HttpStatus.OK);
    }
}
