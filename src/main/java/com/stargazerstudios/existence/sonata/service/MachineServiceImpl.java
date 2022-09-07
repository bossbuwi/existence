package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.database.DependentEntityException;
import com.stargazerstudios.existence.conductor.erratum.database.EntityDeletionErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.input.InvalidInputException;
import com.stargazerstudios.existence.conductor.erratum.input.UnexpectedInputException;
import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
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

    @Autowired
    private AuthorityUtil authorityUtil;

    @Override
    public List<MachineDTO> getAllMachines() {
        List<Machine> machines = machineDAO.findAll();
        List<MachineDTO> machineList = new ArrayList<>();
        if (!machines.isEmpty()) {
            for (Machine machine : machines) {
                machineList.add(machineUtil.outboundMachine(machine));
            }
        }

        return machineList;
    }

    @Override
    public Long getMachineCount() {
        return machineDAO.count();
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
            throws DatabaseErrorException {
        String name = stringUtil.trimToUpper(wMachine.getName());

        Machine machine = new Machine();
        machine.setName(name);

        try {
            machineDAO.saveAndFlush(machine);
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

        return machineUtil.outboundMachine(machine);
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
            machineDAO.saveAndFlush(machine);
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

        return machineUtil.outboundMachine(machine);
    }

    @Override
    public MachineDTO deleteMachine(long id)
            throws AuthorizationErrorException, DatabaseErrorException, EntityErrorException, UnknownInputException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        if (id <= 0) throw new InvalidInputException("id");

        Optional<Machine> machineData = machineDAO.findById(id);
        if (machineData.isEmpty()) throw new EntityNotFoundException("machine", "id", Long.toString(id));

        Machine machine = machineData.get();

        Machine bMachine = new Machine();
        bMachine.setId(machine.getId());
        bMachine.setName(machine.getName());
        bMachine.setSystems(machine.getSystems());
        bMachine.setDateAdded(machine.getDateAdded());
        bMachine.setDateChanged(machine.getDateChanged());

        try {
            machineDAO.delete(machine);
            machineDAO.flush();
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            ConstraintViolationException ex = (ConstraintViolationException) e.getCause();
            String constraint = ex.getConstraintName();

            if (constraint.equals(ConsSonataConstraint.SYSTEM_DEPENDS_ON_MACHINE)) {
                throw new DependentEntityException("machine", machine.getName());
            }

            throw new EntityDeletionErrorException("machine");
        }

        return machineUtil.outboundMachine(bMachine);
    }
}
