package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.sonata.dto.EventTypeDTO;
import com.stargazerstudios.existence.sonata.entity.EventType;
import com.stargazerstudios.existence.sonata.repository.EventTypeDAO;
import com.stargazerstudios.existence.sonata.utils.EventTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventTypeServiceImpl implements EventTypeService {

    @Autowired
    private EventTypeDAO eventTypeDAO;

    @Autowired
    private EventTypeUtil eventTypeUtil;

    @Override
    public List<EventTypeDTO> getAllEvents() {
        List<EventTypeDTO> eventTypeDTOList = new ArrayList<>();
        List<EventType> eventTypes = eventTypeDAO.findAll();

        if (!eventTypes.isEmpty()) {
            for (EventType eventType: eventTypes) {
                EventTypeDTO eventTypeDTO = eventTypeUtil.wrapEventType(eventType);
                eventTypeDTOList.add(eventTypeDTO);
            }
        }
        return eventTypeDTOList;
    }
}
