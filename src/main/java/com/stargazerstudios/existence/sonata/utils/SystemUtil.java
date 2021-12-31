package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.MachineDTO;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Service
public class SystemUtil {

    @Autowired
    private MachineUtil machineUtil;

    public SystemDTO wrapSystem(System system) {
        SystemDTO systemDTO = new SystemDTO();
        systemDTO.setId(system.getId());
        systemDTO.setGlobal_prefix(system.getGlobalPrefix());
        systemDTO.setDescription(system.getDescription());
        systemDTO.setUrl(system.getUrl());
        systemDTO.setOwners(system.getOwners());
        systemDTO.setCreation_date(system.getDateCreated());
        systemDTO.setLast_changed_date(system.getDateChanged());

        Machine machine = system.getMachine();
        systemDTO.setMachine(machine.getName());

        Set<Zone> zoneSet = system.getZones();
        ArrayList<String> zones = new ArrayList<>();

        if (zoneSet != null && !zoneSet.isEmpty()) {
            for (Zone zone: zoneSet) {
                zones.add(zone.getZonalPrefix());
            }
        }
        systemDTO.setZones(zones);
        return systemDTO;
    }

    public SystemDTO stripTimestamps(SystemDTO systemDTO) {
        SystemDTO system;
        system = systemDTO;
        system.setCreation_date(null);
        system.setLast_changed_date(null);
        return system;
    }

    public SystemWrapper populateFields(SystemWrapper wSystem) throws InvalidInputException {
        SystemWrapper systemWrapper;
        systemWrapper = wSystem;
        HashMap<String, String> systemMap = systemWrapper.getSystem();

        if (systemMap.containsKey("id")) {
            try {
                long id = Long.parseLong(systemMap.get("id"));
                systemWrapper.setId(id);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("id");
            }
        }

        if (systemMap.containsKey("global_prefix")) {
            String globalPrefix= systemMap.get("global_prefix").toUpperCase();
            systemWrapper.setGlobal_prefix(globalPrefix);
        } else {
            throw new InvalidInputException("global_prefix");
        }

        if (systemMap.containsKey("machine")) {
            String machine= systemMap.get("machine").toUpperCase();
            systemWrapper.setMachine(machine);
        } else {
            throw new InvalidInputException("machine");
        }

        systemWrapper.setDescription(systemMap.get("description"));
        systemWrapper.setUrl(systemMap.get("url"));
        systemWrapper.setOwners(systemMap.get("owners"));

        return systemWrapper;
    }
}
