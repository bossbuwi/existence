package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class SystemUtil {

    @Autowired
    private MachineUtil machineUtil;

    public SystemDTO wrapSystem(System system) {
        SystemDTO systemDTO = new SystemDTO();
        systemDTO.setId(system.getId());
        systemDTO.setGlobal_prefix(system.getGlobalPrefix());
        systemDTO.setRelease(system.getRelease());
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
}
