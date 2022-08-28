package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import org.springframework.stereotype.Service;

@Service
public class ZoneUtil {

    public ZoneDTO wrapZone(Zone zone) {
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setId(zone.getId());
        zoneDTO.setZonal_prefix(zone.getZonalPrefix());
        zoneDTO.setZone_name(zone.getZoneName());
        zoneDTO.setCreation_date(zone.getDateCreated());
        zoneDTO.setLast_changed_date(zone.getDateChanged());

        System system = zone.getSystem();
        zoneDTO.setSystem(system.getGlobalPrefix());

        Machine machine = system.getMachine();
        zoneDTO.setMachine(machine.getName());

        return zoneDTO;
    }
}
