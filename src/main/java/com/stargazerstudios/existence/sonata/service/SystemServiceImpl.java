package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.repository.MachineDAO;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import com.stargazerstudios.existence.sonata.utils.SystemUtil;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private MachineDAO machineDAO;

    @Autowired
    private SystemUtil systemUtil;

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
            throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        SystemWrapper systemWrapper = systemUtil.populateFields(wSystem);
        Optional<System> systemData = systemDAO.findByGlobalPrefix(systemWrapper.getGlobal_prefix());
        Optional<Machine> machineData = machineDAO.findByName(systemWrapper.getMachine());

        if (!systemData.isPresent()) {
            if (machineData.isPresent()) {
                System system = new System();
                system.setGlobalPrefix(systemWrapper.getGlobal_prefix());
                system.setDescription(systemWrapper.getDescription());
                system.setUrl(systemWrapper.getUrl());
                system.setOwners(systemWrapper.getOwners());
                system.setMachine(machineData.get());
                return systemUtil.wrapSystem(systemDAO.save(system));
            } else {
                throw new EntityNotFoundException("Machine with name: " + systemWrapper.getMachine() + " not found.");
            }
        } else {
            throw new DuplicateEntityException("System with global prefix: " + systemWrapper.getGlobal_prefix() + " already exists.");
        }
    }
}
