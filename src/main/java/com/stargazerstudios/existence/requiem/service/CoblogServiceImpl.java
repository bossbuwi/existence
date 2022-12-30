package com.stargazerstudios.existence.requiem.service;

import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.input.InvalidInputException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.requiem.constants.EnumCoblogConclusion;
import com.stargazerstudios.existence.requiem.dto.CoblogDTO;
import com.stargazerstudios.existence.requiem.entity.Coblog;
import com.stargazerstudios.existence.requiem.repository.CoblogDAO;
import com.stargazerstudios.existence.requiem.utils.CoblogUtil;
import com.stargazerstudios.existence.requiem.wrapper.CoblogWrapper;
import com.stargazerstudios.existence.requiem.wrapper.ComponentWrapper;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.repository.ZoneDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CoblogServiceImpl implements CoblogService {

    @Autowired
    private CoblogDAO coblogDAO;

    @Autowired
    private ZoneDAO zoneDAO;

    @Autowired
    private CoblogUtil coblogUtil;

    @Autowired
    private StringUtil stringUtil;

    @Override
    public List<CoblogDTO> findAllCoblogs() {
        List<CoblogDTO> coblogDTOS = new ArrayList<>();
        List<Coblog> coblogs = coblogDAO.findAll();

        if (!coblogs.isEmpty()) {
            for (Coblog coblog: coblogs) {
                coblogDTOS.add(coblogUtil.wrapCoblog(coblog));
            }
        }

        return coblogDTOS;
    }

    @Override
    public CoblogDTO findCoblogById(long id) throws EntityException {
        Optional<Coblog> coblogData = coblogDAO.findById(id);
        if (coblogData.isEmpty()) throw new EntityNotFoundException("coblog", "id", Long.toString(id));

        Coblog coblog = coblogData.get();
        return coblogUtil.wrapCoblog(coblog);
    }

    @Override
    public CoblogDTO createCoblog(CoblogWrapper wCoblog)
            throws UnknownInputException, DatabaseException, EntityException {
        String description = wCoblog.getDescription();

        String runDayIn = stringUtil.trimToUpper(wCoblog.getRun_day());
        LocalDate runDay;
        try {
            runDay = LocalDate.parse(runDayIn);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("run_day");
        }

        String nextRunDayIn = stringUtil.trimToUpper(wCoblog.getNext_run_day());
        LocalDate nextRunDay;
        try {
            nextRunDay = LocalDate.parse(nextRunDayIn);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("next_run_day");
        }

        long zoneId = wCoblog.getZone_id();
        Optional<Zone> zoneData = zoneDAO.findById(zoneId);
        if (zoneData.isEmpty()) throw new EntityNotFoundException("zone", "id", Long.toString(zoneId));
        Zone zone = zoneData.get();
        System system = zone.getSystem();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        String conclusion = EnumCoblogConclusion.ONGOING.getValue();

        Coblog coblog = new Coblog();
        coblog.setDescription(description);
        coblog.setRunDay(runDay);
        coblog.setNextRunDay(nextRunDay);
        coblog.setActiveUser(username);
        coblog.setCreatedBy(username);
        coblog.setLastChangedBy(username);
        coblog.setZone(zone);
        coblog.setSystem(system);

        coblog.setConclusion(conclusion);
        coblog.setOpen(true);

        try {
            coblogDAO.save(coblog);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveException("coblog");
        }

        return coblogUtil.wrapCoblog(coblog);
    }

    @Override
    public CoblogDTO updateCoblog(CoblogWrapper wCoblog) {
        return null;
    }

    @Override
    public CoblogDTO deleteCoblog(CoblogWrapper wCoblog) {
        return null;
    }

    @Override
    public CoblogDTO attachComponent(ComponentWrapper wComponent) {
        return null;
    }

    @Override
    public CoblogDTO detachComponent(ComponentWrapper wComponent) {
        return null;
    }
}
