package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.MachineDTO;
import com.stargazerstudios.existence.sonata.wrapper.MachineWrapper;

import java.util.List;

public interface MachineService {
    List<MachineDTO> getAllMachines();
    MachineDTO getMachineById(long id) throws EntityNotFoundException;
    MachineDTO getMachineByName(String name) throws EntityNotFoundException;
    MachineDTO createMachine(MachineWrapper machine) throws DuplicateEntityException, InvalidInputException;
    MachineDTO updateMachine(MachineWrapper machine) throws EntityNotFoundException, DuplicateEntityException, InvalidInputException;
    MachineDTO deleteMachine(long id) throws EntityNotFoundException;
}
