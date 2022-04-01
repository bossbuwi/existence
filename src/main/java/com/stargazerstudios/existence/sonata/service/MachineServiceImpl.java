package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.input.UnexpectedInputException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.sonata.constants.ConsSonataConstraint;
import com.stargazerstudios.existence.sonata.dto.MachineDTO;
import com.stargazerstudios.existence.sonata.entity.Machine;
import com.stargazerstudios.existence.sonata.repository.MachineDAO;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import com.stargazerstudios.existence.sonata.utils.MachineUtil;
import com.stargazerstudios.existence.sonata.wrapper.MachineWrapper;
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
public class MachineServiceImpl implements MachineService {

    @Autowired
    private MachineDAO machineDAO;

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private MachineUtil machineUtil;

    @Autowired
    private StringUtil stringUtil;

    @Override
    public List<MachineDTO> getAllMachines() {
        List<Machine> machines = machineDAO.findAll();
        List<MachineDTO> machineList = new ArrayList<>();
        if (!machines.isEmpty()) {
            for (Machine machine : machines) {
                machineList.add(machineUtil.wrapMachine(machine));
            }
        }

        return machineList;
    }

    @Override
    public MachineDTO getMachineById(long id) {
        return null;
    }

    @Override
    public MachineDTO getMachineByName(String name) {
        return null;
    }

    @Override
    public MachineDTO createMachine(MachineWrapper wMachine)
            throws UnknownInputException, DatabaseErrorException {
        String name = stringUtil.trimToUpper(wMachine.getName());

        Machine machine = new Machine();
        machine.setName(name);

        try {
            machineDAO.save(machine);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsSonataConstraint.UNIQUE_MACHINE_NAME))
                throw new DuplicateEntityException("machine", "name", name);
            throw new EntitySaveErrorException("machine");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("machine");
        }

        return machineUtil.wrapMachine(machine);
    }

    @Override
    public MachineDTO updateMachine(MachineWrapper wMachine)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException {
        String name = stringUtil.trimToUpper(wMachine.getName());
        String newName = stringUtil.trimToUpper(wMachine.getNew_name());
        if (name.equals(newName)) throw new UnexpectedInputException("new_name is same as the current name");

        Optional<Machine> machineData = machineDAO.findByName(name);
        if (machineData.isEmpty()) throw new EntityNotFoundException("machine","name", name);

        Machine machine = machineData.get();
        machine.setName(newName);

        try {
            machineDAO.save(machine);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();
            if (constraint.equals(ConsSonataConstraint.UNIQUE_MACHINE_NAME))
                throw new DuplicateEntityException("machine", "name", newName);
            throw new EntitySaveErrorException("machine");
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("machine");
        }

        return machineUtil.wrapMachine(machine);
    }

    @Override
    public MachineDTO deleteMachine(String name) {
        return null;
    }
}
