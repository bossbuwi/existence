package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import com.stargazerstudios.existence.sonata.repository.ZoneDAO;
import com.stargazerstudios.existence.sonata.utils.SystemUtil;
import com.stargazerstudios.existence.sonata.utils.ZoneUtil;
import com.stargazerstudios.existence.sonata.wrapper.ZoneWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ZoneServiceImpl implements ZoneService{

    @Autowired
    private ZoneDAO zoneDAO;

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private ZoneUtil zoneUtil;

    @Autowired
    private SystemUtil systemUtil;

    @Override
    public List<ZoneDTO> getAllZones() {
        List<ZoneDTO> zoneList = new ArrayList<>();
        List<Zone> zones = zoneDAO.findAll();
        if (!zones.isEmpty()) {
            for (Zone zone: zones) {
                ZoneDTO zoneDTO = zoneUtil.wrapZone(zone);
                zoneList.add(zoneDTO);
            }
        }

        return zoneList;
    }

    @Override
    public ZoneDTO createZone(ZoneWrapper wZone)
            throws EntityNotFoundException, DuplicateEntityException, InvalidInputException {

        ZoneWrapper zoneWrapper = zoneUtil.populateFields(wZone);
        Optional<Zone> zoneData = zoneDAO.findByZonalPrefix(zoneWrapper.getZonal_prefix());
        Optional<System> systemData = systemDAO.findByGlobalPrefix(zoneWrapper.getSystem());

        if (!zoneData.isPresent()) {
            if (systemData.isPresent()) {
                Zone zone = new Zone();
                zone.setZonalPrefix(zoneWrapper.getZonal_prefix());
                zone.setZoneName(zoneWrapper.getZone_name());
                zone.setSystem(systemData.get());
                return zoneUtil.wrapZone(zoneDAO.save(zone));
            } else {
                throw new EntityNotFoundException("System with global prefix: " + zoneWrapper.getSystem() + " not found.");
            }
        } else {
            throw new DuplicateEntityException("Zone with prefix: " + zoneWrapper.getZonal_prefix() +
                    " already exists on system with global prefix: " + zoneWrapper.getSystem() + ".");
        }
    }

    @Override
    public ZoneDTO updateZone(ZoneWrapper wZone)
            throws EntityNotFoundException, DuplicateEntityException, InvalidInputException {
        return null;
    }
}
