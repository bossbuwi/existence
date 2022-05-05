package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.wrapper.EventWrapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface EventService {
    List<EventDTO> getAllEvents();
    List<EventDTO> getEventsByDate(String date) throws UnknownInputException;
    List<EventDTO> getEventsBetweenDates(String start, String end) throws UnknownInputException;
    EventDTO getLatestEvent();
    Long getEventCount();
    EventDTO createEvent(EventWrapper event)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException;
    EventDTO updateEvent(EventWrapper event)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException, AuthorizationErrorException;
    void exportEvents(HttpServletResponse response) throws IOException;
}
