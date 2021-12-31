package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.entity.Event;
import com.stargazerstudios.existence.sonata.entity.EventType;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.repository.EventDAO;
import com.stargazerstudios.existence.sonata.repository.EventTypeDAO;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import com.stargazerstudios.existence.sonata.utils.EventUtil;
import com.stargazerstudios.existence.sonata.wrapper.EventWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private EventTypeDAO eventTypeDAO;

    @Autowired
    private EventUtil eventUtil;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public List<EventDTO> getAllEvents() {
        List<EventDTO> eventDTOList = new ArrayList<>();
        List<Event> events = eventDAO.findAll();

        if (!events.isEmpty()) {
            for (Event event : events) {
                EventDTO eventDTO = eventUtil.wrapEvent(event);
                eventDTOList.add(eventDTO);
            }
        }

        return eventDTOList;
    }

    @Override
    public EventDTO getEventByDate(String date) throws EntityNotFoundException {
        return null;
    }

    @Override
    public EventDTO createEvent(EventWrapper eventWrapper)
            throws EntityNotFoundException, InvalidInputException {
        EventWrapper wEvent;
        wEvent = eventWrapper;

        // Create fields to be used for the new event
        System system;
        Set<Zone> zones = new HashSet<>();
        Set<EventType> eventTypes = new HashSet<>();
        Date startDate;
        Date endDate;
        String createdBy;
        String lastChangedBy;

        // For the system
        String wGlobalPrefix = wEvent.getSystem();
        if (wGlobalPrefix != null && !wGlobalPrefix.isEmpty()) {
            Optional<System> systemData = systemDAO.findByGlobalPrefix(wGlobalPrefix);
            if (systemData.isPresent()) {
                system = systemData.get();
            } else {
                throw new EntityNotFoundException("System with global prefix: " + wGlobalPrefix + " not found.");
            }
        } else {
            throw new InvalidInputException("system");
        }

        // For zones
        ArrayList<String> wZonePrefixes = new ArrayList<>(Arrays.asList(wEvent.getZones())); //array from request
        if (hasDuplicates(wZonePrefixes)) throw new InvalidInputException("zones"); //check if array has duplicates
        ArrayList<Zone> dbZones = new ArrayList<>(system.getZones()); //array of zones from database
        ArrayList<String> dbZonePrefixes = new ArrayList<>();

        for (Zone zone : dbZones) {
            dbZonePrefixes.add(zone.getZonalPrefix());
        }

        for (int i = 0; i < wZonePrefixes.size(); i++) {
            if (dbZonePrefixes.contains(wZonePrefixes.get(i))) {
                zones.add(dbZones.get(i));
            } else {
                throw new EntityNotFoundException("System with global prefix "
                        + wGlobalPrefix + " does not have a zone with prefix: " + wZonePrefixes.get(i));
            }
        }

        // For event types
        ArrayList<String> wEventTypes = new ArrayList<>(Arrays.asList(wEvent.getEvent_types())); //array from request
        if (hasDuplicates(wEventTypes)) throw new InvalidInputException("event_types"); //check if array has duplicates
        ArrayList<EventType> dbEventTypes = new ArrayList<>(eventTypeDAO.findAll()); //array of event types from database
        ArrayList<String> dbEventTypeCodes = new ArrayList<>();

        for (EventType eventType : dbEventTypes) {
            dbEventTypeCodes.add(eventType.getCode());
        }

        for (int i = 0; i < wEventTypes.size(); i++) {
            if (dbEventTypeCodes.contains(wEventTypes.get(i))) {
                eventTypes.add(dbEventTypes.get(i));
            } else {
                throw new EntityNotFoundException("Event type with code: "
                        + wEventTypes.get(i) + " not found.");
            }
        }

        // For the start date
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(wEvent.getStart_date());
        } catch (ParseException e) {
            throw new InvalidInputException("start_date");
        }

        // For the end date
        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(wEvent.getEnd_date());
        } catch (ParseException e) {
            throw new InvalidInputException("end_date");
        }

        // For the users
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        createdBy = auth.getName();
        lastChangedBy = auth.getName();

        // Finally, create an event to be saved and returned to the client
        Event event = new Event();
        event.setSystem(system);
        event.setZones(zones);
        event.setEventTypes(eventTypes);
        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setJiraCase(wEvent.getJira_case());
        event.setFeaturesOn(wEvent.getFeatures_on());
        event.setFeaturesOff(wEvent.getFeatures_off());
        event.setCompiledSources(wEvent.getCompiled_sources());
        event.setApiUsed(wEvent.getApi_used());
        event.setApiUsed(wEvent.getApi_used());
        event.setCreatedBy(createdBy);
        event.setLastChangedBy(lastChangedBy);
        return eventUtil.wrapEvent(eventDAO.save(event));
    }

    @Override
    public EventDTO updateEvent(EventWrapper event) throws EntityNotFoundException {
        return null;
    }

    private <T> boolean hasDuplicates(Iterable<T> all) {
        Set<T> set = new HashSet<T>();
        for (T each: all) if (!set.add(each)) return true;
        return false;
    }
}
