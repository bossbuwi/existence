package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.database.EntityDeletionErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.input.InvalidCollectionException;
import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.sonata.constants.ConsSonataConstraint;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.entity.Release;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.repository.MachineDAO;
import com.stargazerstudios.existence.sonata.repository.ReleaseDAO;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import com.stargazerstudios.existence.sonata.utils.SystemUtil;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;
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
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private MachineDAO machineDAO;

    @Autowired
    private ReleaseDAO releaseDAO;

    @Autowired
    private ZoneServiceImpl zoneService;

    @Autowired
    private SystemUtil systemUtil;

    @Autowired
    private StringUtil stringUtil;

    @Autowired
    private AuthorityUtil authorityUtil;

    @Override
    public List<SystemDTO> getSystems() {
        List<System> systems = systemDAO.findAll();
        List<SystemDTO> systemList = new ArrayList<>();

        if (!systems.isEmpty()) {
            for (System system: systems) {
                SystemDTO systemDTO = systemUtil.wrapSystem(system);
                systemList.add(systemDTO);
            }
        }
        return systemList;
    }

    @Override
    public List<SystemDTO> getFullSystems() {
        return null;
    }

    @Override
    public Long countSystems() {
        return systemDAO.count();
    }

    @Override
    public SystemDTO createSystem(SystemWrapper wSystem)
            throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        String globalPrefix = stringUtil.trimToUpper(wSystem.getGlobal_prefix());
        String machineName = stringUtil.trimToUpper(wSystem.getMachine());

        Optional<Machine> machineData = machineDAO.findByName(machineName);
        if (machineData.isEmpty()) throw new EntityNotFoundException("machine", "name", machineName);

        long releaseId = wSystem.getRelease_id();
        Optional<Release> releaseData = releaseDAO.findById(releaseId);
        if (releaseData.isEmpty()) throw new EntityNotFoundException("release", "id", Long.toString(releaseId));

        System system = new System();
        system.setGlobalPrefix(globalPrefix);

        Release release = releaseData.get();
        system.setRelease(release);

        system.setOwners(wSystem.getOwners());
        system.setDescription(wSystem.getDescription());
        system.setUrl(wSystem.getUrl());

        Machine machine = machineData.get();
        system.setMachine(machine);

        try {
            systemDAO.save(system);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsSonataConstraint.UNIQUE_SYSTEM_PER_MACHINE))
                throw new DuplicateEntityException("system", "machine", machineName);
            throw new EntitySaveErrorException("system");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("system");
        }

        return systemUtil.wrapFullSystem(system);
    }

    @Override
    public SystemDTO updateSystem(SystemWrapper wSystem)
            throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        long id = wSystem.getId();
        Optional<System> systemData = systemDAO.findById(id);
        if (systemData.isEmpty()) throw new EntityNotFoundException("system", "id", Long.toString(id));
        System system = systemData.get();

        String machineName = stringUtil.trimToUpper(wSystem.getMachine());
        Optional<Machine> machineData = machineDAO.findByName(machineName);
        if (machineData.isEmpty()) throw new EntityNotFoundException("machine", "name", machineName);
        Machine machine = machineData.get();
        system.setMachine(machine);

        long releaseId = wSystem.getRelease_id();
        Optional<Release> releaseData = releaseDAO.findById(releaseId);
        if (releaseData.isEmpty()) throw new EntityNotFoundException("release", "id", Long.toString(releaseId));

        Release release = releaseData.get();
        system.setRelease(release);
        system.setGlobalPrefix(stringUtil.trimToUpper(wSystem.getGlobal_prefix()));
        system.setDescription(wSystem.getDescription());
        system.setUrl(wSystem.getUrl());
        system.setOwners(wSystem.getOwners());

        try {
            systemDAO.saveAndFlush(system);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();

            if (constraint.equals(ConsSonataConstraint.UNIQUE_SYSTEM_PER_MACHINE)) {
                throw new DuplicateEntityException("system", "machine", machineName);
            }

            throw new EntitySaveErrorException("system");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("system");
        }

        return systemUtil.wrapFullSystem(system);
    }

    @Override
    public SystemDTO deleteSystem(long id)
            throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        Optional<System> systemData = systemDAO.findById(id);
        if (systemData.isEmpty()) throw new EntityNotFoundException("system", "id", Long.toString(id));
        System system = systemData.get();

        try {
            systemDAO.delete(system);
            systemDAO.flush();
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new EntityDeletionErrorException("system");
        }

        return systemUtil.wrapFullSystem(system);
    }

    @Override
    public SystemDTO createFullSystem(SystemWrapper wSystem)
            throws AuthorizationErrorException, DatabaseErrorException, EntityErrorException, UnknownInputException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        if (wSystem.getZones().length == 0) throw new InvalidCollectionException("zones");
        SystemDTO systemDTO = createSystem(wSystem);
        ArrayList<ZoneDTO> zones = new ArrayList<>();
        for (ZoneWrapper zoneItem: wSystem.getZones()) {
            zoneItem.setSystem(wSystem.getGlobal_prefix());
            zoneItem.setMachine(wSystem.getMachine());
            ZoneDTO zoneDTO = zoneService.createZone(zoneItem);
            zones.add(zoneDTO);
        }

        systemDTO.setZones(zones);
        return systemDTO;
    }
}
