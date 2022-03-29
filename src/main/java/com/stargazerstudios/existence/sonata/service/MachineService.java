package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.MachineDTO;
import com.stargazerstudios.existence.sonata.wrapper.MachineWrapper;

import java.util.List;

public interface MachineService {
    List<MachineDTO> getAllMachines();
    MachineDTO getMachineById(long id);
    MachineDTO getMachineByName(String name);
    MachineDTO createMachine(MachineWrapper machine)
            throws UnknownInputException, DatabaseErrorException;
    MachineDTO updateMachine(MachineWrapper machine)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException;
    MachineDTO deleteMachine(String name);
}
