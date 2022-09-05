package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
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

    @Autowired
    private ZoneUtil zoneUtil;

    public SystemDTO wrapFullSystem(System system) {
        SystemDTO systemDTO = wrapSystem(system);
//        systemDTO.setId(system.getId());
//        systemDTO.setGlobal_prefix(system.getGlobalPrefix());
//        systemDTO.setRelease(system.getRelease().getName());
//        systemDTO.setDescription(system.getDescription());
//        systemDTO.setUrl(system.getUrl());
//        systemDTO.setOwners(system.getOwners());
//        systemDTO.setCreation_date(system.getDateCreated());
//        systemDTO.setLast_changed_date(system.getDateChanged());
//
//        Machine machine = system.getMachine();
//        systemDTO.setMachine(machine.getName());
//
        Set<Zone> zoneSet = system.getZones();

        ArrayList<ZoneDTO> zones = new ArrayList<>();
        if (zoneSet != null && !zoneSet.isEmpty()) {
            for (Zone zone: zoneSet) {
                zones.add(zoneUtil.passZoneForTransfer(zone));
            }
        }
        systemDTO.setZones(zones);
        systemDTO.setZone_names(null);
        systemDTO.setZone_prefixes(null);
        return systemDTO;
    }

    public SystemDTO wrapSystem(System system) {
        SystemDTO systemDTO = new SystemDTO();
        systemDTO.setId(system.getId());
        systemDTO.setGlobal_prefix(system.getGlobalPrefix());
        systemDTO.setRelease(system.getRelease().getName());
        systemDTO.setDescription(system.getDescription());
        systemDTO.setUrl(system.getUrl());
        systemDTO.setOwners(system.getOwners());
        systemDTO.setCreation_date(system.getDateCreated());
        systemDTO.setLast_changed_date(system.getDateChanged());

        Machine machine = system.getMachine();
        systemDTO.setMachine(machine.getName());

        Set<Zone> zoneSet = system.getZones();

        ArrayList<String> zoneNames = new ArrayList<>();
        ArrayList<String> zonePrefixes = new ArrayList<>();
        if (zoneSet != null && !zoneSet.isEmpty()) {
            for (Zone zone: zoneSet) {
                zoneNames.add(zone.getZoneName());
                zonePrefixes.add(zone.getZonalPrefix());
            }
        }

        systemDTO.setZone_names(zoneNames);
        systemDTO.setZone_prefixes(zonePrefixes);
        return systemDTO;
    }
}
