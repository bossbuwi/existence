package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.wrapper.ZoneWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ZoneUtil {

    @Autowired
    private SystemUtil systemUtil;

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

    public ZoneWrapper populateFields(ZoneWrapper wZone) throws InvalidInputException {
        ZoneWrapper zoneWrapper;
        zoneWrapper = wZone;
        HashMap<String, String> zoneMap = zoneWrapper.getZone();

        if (zoneMap.containsKey("id")) {
            try {
                long id = Long.parseLong(zoneMap.get("id"));
                zoneWrapper.setId(id);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("id");
            }
        }

        if (zoneMap.containsKey("zonal_prefix")) {
            String zonalPrefix= zoneMap.get("zonal_prefix").toUpperCase();
            zoneWrapper.setZonal_prefix(zonalPrefix);
        } else {
            throw new InvalidInputException("zonal_prefix");
        }

        if (zoneMap.containsKey("system")) {
            String system= zoneMap.get("system").toUpperCase();
            zoneWrapper.setSystem(system);
        } else {
            throw new InvalidInputException("system");
        }

        zoneWrapper.setZone_name(zoneMap.get("zone_name"));

        return zoneWrapper;
    }
}
