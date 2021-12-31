package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.entity.Event;
import com.stargazerstudios.existence.sonata.entity.EventType;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.wrapper.EventWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Service
public class EventUtil {

    public EventDTO wrapEvent(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(event.getId());
        eventDTO.setStart_date(event.getStartDate());
        eventDTO.setEnd_date(event.getEndDate());
        eventDTO.setJira_case(event.getJiraCase());
        eventDTO.setFeatures_on(event.getFeaturesOn());
        eventDTO.setFeatures_off(event.getFeaturesOff());
        eventDTO.setCompiled_sources(event.getCompiledSources());
        eventDTO.setApi_used(event.getApiUsed());
        eventDTO.setCreated_by(event.getCreatedBy());
        eventDTO.setCreation_date(event.getDateCreated());
        eventDTO.setLast_changed_by(event.getLastChangedBy());
        eventDTO.setLast_changed_date(event.getDateChanged());

        System system = event.getSystem();
        eventDTO.setSystem(system.getGlobalPrefix());

        Set<Zone> zoneSet = event.getZones();
        ArrayList<String> zones = new ArrayList<>();
        if (zoneSet != null && !zoneSet.isEmpty()) {
            for (Zone zone: zoneSet) {
                zones.add(zone.getZonalPrefix());
            }
        }
        eventDTO.setZones(zones);

        Set<EventType> eventTypeSet = event.getEventTypes();
        ArrayList<String> eventTypes = new ArrayList<>();
        if (eventTypeSet != null && !eventTypeSet.isEmpty()) {
            for (EventType eventType: eventTypeSet) {
                eventTypes.add(eventType.getCode());
            }
        }
        eventDTO.setEvent_types(eventTypes);

        return eventDTO;
    }

    public EventWrapper populateFields(EventWrapper wEvent) throws InvalidInputException {
        EventWrapper eventWrapper;
        eventWrapper = wEvent;
        HashMap<String, String> eventMap = eventWrapper.getEvent();

        if (eventMap.containsKey("id")) {
            try {
                long id = Long.parseLong(eventMap.get("id"));
                wEvent.setId(id);
            } catch (NumberFormatException e) {
                throw new InvalidInputException("id");
            }
        }

        return eventWrapper;
    }
}
