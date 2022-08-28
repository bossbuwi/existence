package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
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
                ZoneDTO zoneDTO = zoneUtil.wrapZone(zone);
                zoneList.add(zoneDTO);
            }
        }

        return zoneList;
    }

    @Override
    public ZoneDTO createZone(ZoneWrapper wZone)
            throws AuthorizationErrorException, UnknownInputException, EntityErrorException, DatabaseErrorException {
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
            zoneDAO.save(zone);
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

        return zoneUtil.wrapZone(zone);
    }

    @Override
    public ZoneDTO updateZone(ZoneWrapper wZone) {
        return null;
    }
}
