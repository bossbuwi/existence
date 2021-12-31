package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.wrapper.EventWrapper;

import java.util.List;

public interface EventService {
    List<EventDTO> getAllEvents();
    EventDTO getEventByDate(String date) throws EntityNotFoundException;
    EventDTO createEvent(EventWrapper event) throws EntityNotFoundException, InvalidInputException;
    EventDTO updateEvent(EventWrapper event) throws EntityNotFoundException;
}
