package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.MachineDTO;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.wrapper.MachineWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class MachineUtil {

    public MachineDTO wrapMachine(Machine machine, boolean isStandalone) {
        MachineDTO machineDTO = new MachineDTO();
        List<SystemDTO> systems = new ArrayList<>();
        machineDTO.setId(machine.getId());
        machineDTO.setName(machine.getName());
        machineDTO.setCreation_date(machine.getDateAdded());
        machineDTO.setLast_changed_date(machine.getDateChanged());

        if (!isStandalone) {
            for (System system : machine.getSystems()) {
                SystemDTO systemDTO = new SystemDTO();
                systemDTO.setId(system.getId());
                systemDTO.setGlobal_prefix(system.getGlobalPrefix());
                systemDTO.setDescription(system.getDescription());
                systemDTO.setUrl(system.getUrl());
                systemDTO.setOwners(system.getOwners());
                systems.add(systemDTO);
            }
            machineDTO.setSystems(systems);
        }

        return machineDTO;
    }

    public MachineDTO stripTimestamps(MachineDTO machineDTO) {
        MachineDTO machine;
        machine = machineDTO;
        machine.setCreation_date(null);
        machine.setLast_changed_date(null);
        return machine;
    }

    public MachineWrapper populateFields(MachineWrapper wMachine) throws InvalidInputException {
        MachineWrapper machineWrapper;
        machineWrapper = wMachine;
        HashMap<String, String> machineMap = machineWrapper.getMachine();
        if (machineMap.containsKey("id")) {
            try {
                long id = Long.parseLong(machineMap.get("id"));
                machineWrapper.setId(id);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("id");
            }
        }

        if (machineMap.containsKey("name")) {
            String name= machineMap.get("name").toUpperCase();
            machineWrapper.setName(name);
        } else {
            throw new InvalidInputException("name");
        }

        return machineWrapper;
    }
}
