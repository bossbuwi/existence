package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.MachineDTO;
import com.stargazerstudios.existence.sonata.wrapper.MachineWrapper;

import java.util.List;

public interface MachineService {
    List<MachineDTO> getAllMachines();
    Long getMachineCount();
    MachineDTO getMachineById(long id);
    MachineDTO getMachineByName(String name);
    MachineDTO createMachine(MachineWrapper machine)
            throws DatabaseException;
    MachineDTO updateMachine(MachineWrapper machine)
            throws UnknownInputException, EntityException, DatabaseException;
    MachineDTO deleteMachine(long id) throws AuthorizationException, UnknownInputException, EntityException, DatabaseException;
}
