package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.sonata.constants.ConsSonataConstraint;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import com.stargazerstudios.existence.sonata.repository.ZoneDAO;
import com.stargazerstudios.existence.sonata.utils.SystemUtil;
import com.stargazerstudios.existence.sonata.utils.ZoneUtil;
import com.stargazerstudios.existence.sonata.wrapper.ZoneWrapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ZoneServiceImpl implements ZoneService{

    @Autowired
    private ZoneDAO zoneDAO;

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private ZoneUtil zoneUtil;

    @Autowired
    private SystemUtil systemUtil;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private AuthorityUtil authorityUtil;

    @Override
    public List<ZoneDTO> getAllZones() {
        List<ZoneDTO> zoneList = new ArrayList<>();
        List<Zone> zones = zoneDAO.findAll();
        if (!zones.isEmpty()) {
            for (Zone zone: zones) {
                ZoneDTO zoneDTO = zoneUtil.outboundZone(zone);
                zoneList.add(zoneDTO);
            }
        }

        return zoneList;
    }

    @Override
    public ZoneDTO createZone(ZoneWrapper wZone)
            throws AuthorizationErrorException, EntityErrorException, DatabaseErrorException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        String zonalPrefix = stringUtil.trimToUpper(wZone.getZonal_prefix());
        String zoneName = stringUtil.trimToUpper(wZone.getZone_name());
        String systemName = stringUtil.trimToUpper(wZone.getSystem());
        String machineName = stringUtil.trimToUpper(wZone.getMachine());

        Optional<System> systemData = systemDAO.findSystemOnMachine(systemName, machineName);
        if (systemData.isEmpty()) throw new EntityNotFoundException("system", "global_prefix", systemName);

        Zone zone = new Zone();
        zone.setZoneName(zoneName);
        zone.setZonalPrefix(zonalPrefix);
        zone.setSystem(systemData.get());

        try {
            zoneDAO.saveAndFlush(zone);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsSonataConstraint.UNIQUE_ZONE_PER_SYSTEM))
                throw new DuplicateEntityException("zone", "zonal_prefix", zonalPrefix);
            throw new EntitySaveErrorException("zone");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("zone");
        }

        return zoneUtil.outboundZone(zone);
    }

    @Override
    public ZoneDTO updateZone(ZoneWrapper wZone)
            throws AuthorizationErrorException, EntityErrorException, DatabaseErrorException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        long id = wZone.getId();
        String zonalPrefix = stringUtil.trimToUpper(wZone.getZonal_prefix());
        String zoneName = stringUtil.trimToUpper(wZone.getZone_name());

        Optional<Zone> zoneData = zoneDAO.findById(id);

        // TODO Read below rant.
        // If the zone is not found on the system, it needs to be created.
        // This raises a problem, however.
        // If there are 2 zones with name A1 and A2 and the user renamed A1 to A3, A3 would be created.
        // However, A1 stills exists. In reality, it needs to be deleted because it was changed to A3.
        if (zoneData.isEmpty()) {
            return createZone(wZone);
        } else {
            Zone zone = zoneData.get();
            zone.setZoneName(zoneName);
            zone.setZonalPrefix(zonalPrefix);

            try {
                zoneDAO.saveAndFlush(zone);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return zoneUtil.outboundZone(zone);
        }
    }

    @Override
    public ZoneDTO deleteZone(ZoneWrapper wZone) {

        return null;
    }

    @Override
    public ZoneDTO deleteZone(Zone zone) {
        // TODO This needs to be finished because updateFullSystem is dependent on it.
        // This is just an internal method. It must not be exposed on any endpoint.
        // Several complications can arise from this, especially because zones cannot be deleted if events are using them
        return null;
    }
}
