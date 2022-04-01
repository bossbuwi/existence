package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.sonata.constants.ConsSonataConstraint;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.repository.MachineDAO;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import com.stargazerstudios.existence.sonata.utils.SystemUtil;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;
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
    private SystemUtil systemUtil;

    @Autowired
    private StringUtil stringUtil;

    @Override
    public List<SystemDTO> getAllSystems() {
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
    public SystemDTO createSystem(SystemWrapper wSystem)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException {
        String globalPrefix = stringUtil.trimToUpper(wSystem.getGlobal_prefix());
        String machineName = stringUtil.trimToUpper(wSystem.getMachine());

        Optional<Machine> machineData = machineDAO.findByName(machineName);
        if (machineData.isEmpty()) throw new EntityNotFoundException("machine", "name", machineName);

        System system = new System();
        system.setGlobalPrefix(globalPrefix);
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

        return systemUtil.wrapSystem(system);
    }
}
