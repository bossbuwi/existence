package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.sonata.dto.MachineDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.entity.System;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MachineUtil {

    public MachineDTO wrapMachine(Machine machine) {
        MachineDTO machineDTO = new MachineDTO();
        machineDTO.setId(machine.getId());
        machineDTO.setName(machine.getName());
        machineDTO.setCreation_date(machine.getDateAdded());
        machineDTO.setLast_changed_date(machine.getDateChanged());

        Set<System> systems = machine.getSystems();
        List<String> systemsList = new ArrayList<>();
        if (systems != null && systems.size() > 0) {
            for (System system : systems) {
                systemsList.add(system.getGlobalPrefix());
            }
        }
        machineDTO.setSystems(systemsList);
        return machineDTO;
    }
}
