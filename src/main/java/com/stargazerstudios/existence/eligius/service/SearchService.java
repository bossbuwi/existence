package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.conductor.erratum.root.SystemException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.eligius.wrapper.EventFilterWrapper;
import com.stargazerstudios.existence.sonata.dto.EventDTO;

import java.util.List;

public interface SearchService {
    List<EventDTO> filterEvents(EventFilterWrapper eventFilterWrapper) throws UnknownInputException;
    List<EventDTO> searchEvents(String keyword) throws SystemException;
}
