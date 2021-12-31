package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.MachineDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.repository.MachineDAO;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import com.stargazerstudios.existence.sonata.utils.MachineUtil;
import com.stargazerstudios.existence.sonata.wrapper.MachineWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class MachineServiceImpl implements MachineService {

    @Autowired
    private MachineDAO machineDAO;

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private MachineUtil machineUtil;

    @Override
    public List<MachineDTO> getAllMachines() {
        List<Machine> machines = machineDAO.findAll();
        List<MachineDTO> machineList = new ArrayList<>();
        if (!machines.isEmpty()) {
            for (Machine machine: machines) {
                MachineDTO machineDTO = machineUtil.wrapMachine(machine, true);
                machineList.add(machineDTO);
            }
        }
        return machineList;
    }

    @Override
    public MachineDTO getMachineById(long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public MachineDTO getMachineByName(String name) throws EntityNotFoundException {
        return null;
    }

    @Override
    public MachineDTO createMachine(MachineWrapper wMachine) throws DuplicateEntityException, InvalidInputException {
        MachineWrapper machineWrapper = machineUtil.populateFields(wMachine);
        Optional<Machine> machineData = machineDAO.findByName(machineWrapper.getName());
        if (machineData.isPresent()) {
            throw new DuplicateEntityException("Machine with name: " + machineWrapper.getName() + " already exists.");
        } else {
            Machine machine = new Machine();
            machine.setName(machineWrapper.getName());
            return machineUtil.wrapMachine(machineDAO.save(machine), true);
        }
    }

    @Override
    public MachineDTO updateMachine(MachineWrapper wMachine)
            throws EntityNotFoundException, DuplicateEntityException, InvalidInputException {
        MachineWrapper machineWrapper = machineUtil.populateFields(wMachine);
        Optional<Machine> machineData = machineDAO.findById(machineWrapper.getId());
        Optional<Machine> newMachine = machineDAO.findByName(machineWrapper.getName());
        if (machineData.isPresent()) {
            if (newMachine.isPresent()) {
                throw new DuplicateEntityException("Machine with name: " + machineWrapper.getName() + " already exists.");
            } else {
                Machine machine = machineData.get();
                machine.setName(machineWrapper.getName());
                return machineUtil.wrapMachine(machineDAO.save(machine), true);
            }
        } else {
            throw new EntityNotFoundException("Machine with id: " + machineWrapper.getId() + " not found.");
        }
    }

    @Override
    public MachineDTO deleteMachine(long id) throws EntityNotFoundException {
        return null;
    }
}
