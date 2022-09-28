package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.eligius.wrapper.EventFilterWrapper;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.entity.Event;
import com.stargazerstudios.existence.sonata.repository.EventDAO;
import com.stargazerstudios.existence.sonata.utils.EventUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.stargazerstudios.existence.eligius.specs.EventSpecs.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional(rollbackFor = Exception.class)
public class SearchServiceImpl implements SearchService {

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private EventUtil eventUtil;

    @Override
    public List<EventDTO> filterEvents(EventFilterWrapper eventFilterWrapper) {
        Specification<Event> dateSpec = where(betweenDateLiterals(eventFilterWrapper.getStart_date(), eventFilterWrapper.getEnd_date()))
                .or(betweenDateFields(eventFilterWrapper.getStart_date(), eventFilterWrapper.getEnd_date()));
        Specification<Event> technicalSpec = where(withId(eventFilterWrapper.getId()))
                .and(withMachine(eventFilterWrapper.getMachine()))
                .and(withSystem(eventFilterWrapper.getGlobal_prefix()));
        Specification<Event> detailSpec = where(withJiraCase(eventFilterWrapper.getJira_case()))
                .and(withFeaturesOn(eventFilterWrapper.getFeatures_on()))
                .and(withFeaturesOff(eventFilterWrapper.getFeatures_off()))
                .and(withCompiledSources(eventFilterWrapper.getCompiled_sources()))
                .and(withApiUsed(eventFilterWrapper.getApi_used()))
                .and(withCreatedBy(eventFilterWrapper.getCreated_by()))
                .and(withLastChangedBy(eventFilterWrapper.getLast_modified_by()));
        Specification<Event> finalSpec = dateSpec.and(technicalSpec).and(detailSpec);
        List<Event> events = eventDAO.findAll(finalSpec);

        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event: events) {
            eventDTOs.add(eventUtil.wrapEvent(event));
        }

        return eventDTOs;
    }

    @Override
    public List<EventDTO> searchEvents(String keyword) {
        List<Event> events = eventDAO.search(keyword);
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event: events) {
            eventDTOs.add(eventUtil.wrapEvent(event));
        }

        return eventDTOs;
    }
}
