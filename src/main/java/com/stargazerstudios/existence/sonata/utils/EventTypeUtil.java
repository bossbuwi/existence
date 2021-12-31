package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.sonata.dto.EventTypeDTO;
import com.stargazerstudios.existence.sonata.entity.EventType;
import org.springframework.stereotype.Service;

@Service
public class EventTypeUtil {

    public EventTypeDTO wrapEventType(EventType eventType) {
        EventTypeDTO eventTypeDTO = new EventTypeDTO();
        eventTypeDTO.setId(eventTypeDTO.getId());
        eventTypeDTO.setCode(eventType.getCode());
        eventTypeDTO.setName(eventType.getName());
        eventTypeDTO.setDescription(eventType.getDescription());
        eventTypeDTO.setExclusive(eventType.isExclusive());
        eventTypeDTO.setCreation_date(eventType.getDateAdded());
        eventTypeDTO.setLast_changed_date(eventType.getDateChanged());
        return eventTypeDTO;
    }
}
