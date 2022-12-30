package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
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
            throws UnknownInputException, EntityException, DatabaseException;
    EventDTO updateEvent(EventWrapper event)
            throws UnknownInputException, EntityException, DatabaseException, AuthorizationException;
    boolean deleteEvent(long id)
            throws AuthorizationException, DatabaseException, EntityException;
    void exportEvents(HttpServletResponse response) throws IOException;
    Long getNumberOfEventsByUser(long id) throws EntityException;
    EventDTO getLatestEventByUser(String username) throws EntityException;
    void generateTSVData(long id) throws UnknownInputException;
}
