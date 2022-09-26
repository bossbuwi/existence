package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.constants.EnumAuthorization;
import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.authorization.UserUnauthorizedException;
import com.stargazerstudios.existence.conductor.erratum.database.EntityDeletionErrorException;
import com.stargazerstudios.existence.conductor.erratum.database.EntitySaveErrorException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.entity.EntityRelationErrorException;
import com.stargazerstudios.existence.conductor.erratum.input.EmptyInputException;
import com.stargazerstudios.existence.conductor.erratum.input.InvalidInputException;
import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.utils.AuthorityUtil;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.entity.Event;
import com.stargazerstudios.existence.sonata.entity.EventType;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.repository.EventDAO;
import com.stargazerstudios.existence.sonata.repository.EventTypeDAO;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import com.stargazerstudios.existence.sonata.utils.EventExporterUtil;
import com.stargazerstudios.existence.sonata.utils.EventUtil;
import com.stargazerstudios.existence.sonata.wrapper.EventWrapper;
import com.stargazerstudios.existence.symphony.entity.User;
import com.stargazerstudios.existence.symphony.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class EventServiceImpl implements EventService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private EventTypeDAO eventTypeDAO;

    @Autowired
    private EventUtil eventUtil;

    @Autowired
    private EventExporterUtil exporterUtil;

    @Autowired
    private AuthorityUtil authorityUtil;

    @Autowired
    private StringUtil stringUtil;

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
    public List<EventDTO> getEventsByDate(String date) throws UnknownInputException {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("date");
        }

        List<EventDTO> eventsList = new ArrayList<>();
        List<Event> events = eventDAO.findEventsByDate(localDate);
        if (!events.isEmpty()) {
            for (Event event: events) {
                EventDTO eventDTO = eventUtil.wrapEvent(event);
                eventsList.add(eventDTO);
            }
        }
        return eventsList;
    }

    @Override
    public List<EventDTO> getEventsBetweenDates(String start, String end) throws UnknownInputException {
        LocalDate startDate;
        LocalDate endDate;

        try {
            String sDate = start.substring(0, 10);
            String eDate = end.substring(0, 10);
            startDate = LocalDate.parse(sDate);
            endDate = LocalDate.parse(eDate);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("date");
        }

        List<EventDTO> eventsList = new ArrayList<>();
        List<Event> events = eventDAO.findEventsBetweenDates(startDate, endDate);
        if (!events.isEmpty()) {
            for (Event event: events) {
                EventDTO eventDTO = eventUtil.wrapEvent(event);
                eventsList.add(eventDTO);
            }
        }
        return eventsList;
    }

    @Override
    public EventDTO getLatestEvent() {
        Optional<Event> eventData = eventDAO.findFirstByOrderByDateCreatedDesc();
        return eventData.map(event -> eventUtil.wrapEvent(event)).orElse(null);
    }

    @Override
    public Long getEventCount() {
        return eventDAO.count();
    }

    @Override
    public EventDTO createEvent(EventWrapper wEvent)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException {
        String startDateIn = stringUtil.trimToUpper(wEvent.getStart_date());
        String endDateIn = stringUtil.trimToUpper(wEvent.getEnd_date());
        String globalPrefix = stringUtil.trimToUpper(wEvent.getGlobal_prefix());
        String machine = stringUtil.trimToUpper(wEvent.getMachine());

        LocalDate startDate;
        try {
            startDate = LocalDate.parse(startDateIn);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("start_date");
        }

        LocalDate endDate;
        try {
            endDate = LocalDate.parse(endDateIn);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("end_date");
        }

        if (endDate.isBefore(startDate)) throw new InvalidInputException("end_date");

        String[] eventTypesArr = wEvent.getEvent_types();
        Set<String> eventTypeQuery = new HashSet<>();
        for (String item: eventTypesArr) {
            String out = stringUtil.checkInputTrimToUpper(item);
            if (out.equals(EnumUtilOutput.EMPTY.getValue()))
                throw new EmptyInputException("event_types");
            eventTypeQuery.add(out);
        }

        String[] zonesArr = wEvent.getZones();
        Set<String> zoneQuery = new HashSet<>();
        for (String item: zonesArr) {
            String out = stringUtil.checkInputTrimToUpper(item);
            if (out.equals(EnumUtilOutput.EMPTY.getValue()))
                throw new EmptyInputException("zones");
            zoneQuery.add(out);
        }

        Optional<System> systemData = systemDAO.findSystemOnMachine(globalPrefix, machine);
        if (systemData.isEmpty())
            throw new EntityNotFoundException("system", "global_prefix", globalPrefix, "machine", "name", machine);
        System system = systemData.get();
        if (system.getZones().size() == 0)
            throw new EntityRelationErrorException("System: " + globalPrefix + " on machine: " + machine +
                    " does not have zones.");
        List<Zone> zoneDb = new ArrayList<>(system.getZones());
        Set<Zone> zones = new HashSet<>();
        for (String item : zoneQuery) {
            Zone zone = zoneDb.stream()
                    .filter(listItem -> item.equals(listItem.getZonalPrefix()))
                    .findAny()
                    .orElse(null);
            if (zone != null) {
                zones.add(zone);
            } else {
                throw new EntityNotFoundException("zone", "zonal_prefix", item, "system", "global_prefix", globalPrefix);
            }
        }

        List<EventType> eventTypeDb = eventTypeDAO.findByCodeSet(eventTypeQuery);
        List<EventType> exclusives = eventTypeDb.stream()
                .filter(EventType::isExclusive)
                .toList();
        if (exclusives.size() > 1) throw new EntityErrorException("Only one exclusive event type may be used.");
        Set<EventType> eventTypes = new HashSet<>(eventTypeDb);

        Event event = new Event();

        if (wEvent.is_imported()) {
            String createdBy = wEvent.getCreated_by();
            String modifiedBy = wEvent.getLast_modified_by();
            event.setCreatedBy(createdBy);
            event.setLastChangedBy(modifiedBy);
        } else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            event.setCreatedBy(username);
            event.setLastChangedBy(username);
        }

        event.setStartDate(startDate);
        event.setEndDate(endDate);
        event.setSystem(system);
        event.setZones(zones);
        event.setEventTypes(eventTypes);

        event.setJiraCase(wEvent.getJira_case());
        event.setFeaturesOn(wEvent.getFeatures_on());
        event.setFeaturesOff(wEvent.getFeatures_off());
        event.setCompiledSources(wEvent.getCompiled_sources());
        event.setApiUsed(wEvent.getApi_used());

        try {
            eventDAO.save(event);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("event");
        }

        return eventUtil.wrapEvent(event);
    }

    @Override
    public EventDTO updateEvent(EventWrapper wEvent)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException, AuthorizationErrorException {
        // TODO This method is too bloated. It is a PUT method but requires the user to send back all of the existing
        //  property that the entity have.
        //  A better and more efficient approach would be if the user is allowed to send only the properties that needs
        //  to be updated. This needs a rework, but is of low priority.
        long id = wEvent.getId();

        String globalPrefixIn = null;
        String machineIn = null;
        String startDateIn = null;
        String endDateIn = null;
        String[] zonesIn = null;
        String[] eventTypeIn = null;

        // Check non-nullable inputs
        if (wEvent.getGlobal_prefix() != null) {
            globalPrefixIn = stringUtil.checkInputTrimToUpper(wEvent.getGlobal_prefix());
            if (globalPrefixIn.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("global_prefix");
        }

        if (wEvent.getMachine() != null) {
            machineIn = stringUtil.checkInputTrimToUpper(wEvent.getMachine());
            if (machineIn.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("machine");
        }

        if (wEvent.getStart_date() != null) {
            startDateIn = stringUtil.checkInputTrimToUpper(wEvent.getStart_date());
            if (startDateIn.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("start_date");
        }

        if (wEvent.getEnd_date() != null) {
            endDateIn = stringUtil.checkInputTrimToUpper(wEvent.getStart_date());
            if (endDateIn.equals(EnumUtilOutput.EMPTY.getValue())) throw new InvalidInputException("end_date");
        }

        // Check non-nullable arrays
        if (wEvent.getZones() != null) {
            if (wEvent.getZones().length == 0) throw new InvalidInputException("zones");
            zonesIn = wEvent.getZones();
        }

        if (wEvent.getEvent_types() != null) {
            if (wEvent.getEvent_types().length == 0) throw new InvalidInputException("event_types");
            eventTypeIn = wEvent.getEvent_types();
        }

        Optional<Event> eventData = eventDAO.findById(id);
        if (eventData.isEmpty()) throw new EntityNotFoundException("event", "id", Long.toString(id));

        Event event = eventData.get();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());

        if (!username.equals(event.getCreatedBy()) || !isAuthorized) throw new UserUnauthorizedException();
        event.setLastChangedBy(username);

        if (startDateIn != null && endDateIn != null) {
            LocalDate startDate;
            try {
                startDate = LocalDate.parse(startDateIn);
            } catch (DateTimeParseException e) {
                throw new InvalidInputException("start_date");
            }

            LocalDate endDate;
            try {
                endDate = LocalDate.parse(endDateIn);
            } catch (DateTimeParseException e) {
                throw new InvalidInputException("end_date");
            }

            if (endDate.isBefore(startDate)) throw new InvalidInputException("end_date");

            event.setStartDate(startDate);
            event.setEndDate(endDate);
        }

        System system = null;
        if (globalPrefixIn != null && machineIn != null) {
            Optional<System> systemData = systemDAO.findSystemOnMachine(globalPrefixIn, machineIn);
            if (systemData.isEmpty())
                throw new EntityNotFoundException("system", "global_prefix", globalPrefixIn, "machine", "name", machineIn);
            system = systemData.get();
            event.setSystem(system);
        }

        if (zonesIn != null) {
            if (system == null) {
                system = event.getSystem();

                Set<String> zoneQuery = new HashSet<>();
                for (String item: zonesIn) {
                    String out = stringUtil.checkInputTrimToUpper(item);
                    if (out.equals(EnumUtilOutput.EMPTY.getValue()))
                        throw new EmptyInputException("zones");
                    zoneQuery.add(out);
                }

                List<Zone> zoneDb = new ArrayList<>(system.getZones());
                Set<Zone> zones = new HashSet<>();
                for (String item : zoneQuery) {
                    Zone zone = zoneDb.stream()
                            .filter(listItem -> item.equals(listItem.getZonalPrefix()))
                            .findAny()
                            .orElse(null);
                    if (zone != null) {
                        zones.add(zone);
                    } else {
                        throw new EntityNotFoundException("zone", "zonal_prefix", item, "system", "global_prefix", system.getGlobalPrefix());
                    }
                }
                event.setZones(zones);
            } else {
                List<Zone> zoneDb = new ArrayList<>(system.getZones());
                Set<Zone> zones = new HashSet<>();
                for (String item : zonesIn) {
                    Zone zone = zoneDb.stream()
                            .filter(listItem -> item.equals(listItem.getZonalPrefix()))
                            .findAny()
                            .orElse(null);
                    if (zone != null) {
                        zones.add(zone);
                    } else {
                        throw new EntityNotFoundException("zone", "zonal_prefix", item, "system", "global_prefix", globalPrefixIn);
                    }
                }
                event.setZones(zones);
            }
        }

        if (eventTypeIn != null) {
            Set<String> eventTypeQuery = new HashSet<>();
            for (String item: eventTypeIn) {
                String out = stringUtil.checkInputTrimToUpper(item);
                if (out.equals(EnumUtilOutput.EMPTY.getValue()))
                    throw new EmptyInputException("event_types");
                eventTypeQuery.add(out);
            }

            List<EventType> eventTypeDb = eventTypeDAO.findByCodeSet(eventTypeQuery);
            List<EventType> exclusives = eventTypeDb.stream()
                    .filter(EventType::isExclusive)
                    .toList();
            if (exclusives.size() > 1) throw new EntityErrorException("Only one exclusive event type may be used.");
            Set<EventType> eventTypes = new HashSet<>(eventTypeDb);
            event.setEventTypes(eventTypes);
        }

        if (wEvent.getJira_case() != null) event.setJiraCase(wEvent.getJira_case());
        if (wEvent.getFeatures_on() != null) event.setJiraCase(wEvent.getFeatures_on());
        if (wEvent.getFeatures_off() != null) event.setJiraCase(wEvent.getFeatures_off());
        if (wEvent.getCompiled_sources() != null) event.setJiraCase(wEvent.getCompiled_sources());
        if (wEvent.getApi_used() != null) event.setJiraCase(wEvent.getApi_used());

        try {
            eventDAO.save(event);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntitySaveErrorException("event");
        }

        return eventUtil.wrapEvent(event);
    }

    @Override
    public EventDTO deleteEvent(long id)
            throws DatabaseErrorException, EntityErrorException, AuthorizationErrorException {
        boolean isAuthorized = authorityUtil.checkAuthority(EnumAuthorization.ADMIN.getValue());
        if (!isAuthorized) throw new UserUnauthorizedException();

        Optional<Event> eventData = eventDAO.findById(id);
        if (eventData.isEmpty()) throw new EntityNotFoundException("event", "id", Long.toString(id));

        Event event = eventData.get();
        try {
            eventDAO.delete(event);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EntityDeletionErrorException("event");
        }
        return eventUtil.wrapEvent(event);
    }

    @Override
    public void exportEvents(HttpServletResponse response) throws IOException {
        List<Event> events = eventDAO.findAll();
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=events_" + currentDateTime + ".xlsx";
        response.addHeader(headerKey, headerValue);
        exporterUtil.exportToWorkbook(response, events);
    }

    @Override
    public Long getNumberOfEventsByUser(long id) throws EntityErrorException {
        Optional<User> userData = userDAO.findById(id);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "id", Long.toString(id));

        User user = userData.get();
        return eventDAO.countByCreatedBy(user.getUsername());
    }

    @Override
    public EventDTO getLatestEventByUser(String username) throws EntityErrorException {
        Optional<User> userData = userDAO.findByUsername(username);
        if (userData.isEmpty()) throw new EntityNotFoundException("user", "username", username);

        Optional<Event> eventData = eventDAO.findFirstByCreatedByOrderByDateCreatedDesc(username);
        return eventData.map(event -> eventUtil.wrapEvent(event)).orElse(null);
    }
}
