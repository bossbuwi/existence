package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.eligius.wrapper.EventFilterWrapper;
import com.stargazerstudios.existence.sonata.dto.EventDTO;

import java.util.List;

public interface SearchService {
    List<EventDTO> filterEvents(EventFilterWrapper eventFilterWrapper);
}
