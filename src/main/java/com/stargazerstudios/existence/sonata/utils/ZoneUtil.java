package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.wrapper.ZoneWrapper;
import org.springframework.stereotype.Service;

@Service
public class ZoneUtil {

    public ZoneDTO passZoneForTransfer(Zone zone) {
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

    public ZoneWrapper wrapZone(Zone zone) {
        ZoneWrapper zoneWrapper = new ZoneWrapper();
        zoneWrapper.setId(zone.getId());
        zoneWrapper.setMachine(zone.getSystem().getMachine().getName());
        zoneWrapper.setSystem(zone.getSystem().getGlobalPrefix());
        zoneWrapper.setZone_name(zone.getZoneName());
        zoneWrapper.setZonal_prefix(zone.getZonalPrefix());
        return zoneWrapper;
    }
}
