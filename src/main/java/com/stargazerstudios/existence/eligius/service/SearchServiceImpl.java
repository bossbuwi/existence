package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.conductor.constants.EnumUtilOutput;
import com.stargazerstudios.existence.conductor.erratum.input.InvalidInputException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.erratum.system.SearchNotImplementedException;
import com.stargazerstudios.existence.conductor.utils.StringUtil;
import com.stargazerstudios.existence.eligius.wrapper.EventFilterWrapper;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.entity.Event;
import com.stargazerstudios.existence.sonata.repository.EventDAO;
import com.stargazerstudios.existence.sonata.utils.EventUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    @Autowired
    private StringUtil stringUtil;

    @Override
    public List<EventDTO> filterEvents(EventFilterWrapper eventFilterWrapper) throws UnknownInputException {
        // Dates are a bit sensitive so extra precaution must be included
        // to assure that the dates are either in acceptable format or null
        // any other values must not be accepted or be transformed to usable format
        String startDate = stringUtil.checkInputTrim(eventFilterWrapper.getStart_date());
        String endDate = stringUtil.checkInputTrim(eventFilterWrapper.getEnd_date());

        // Check both dates if they are not blanks or if they are in the correct format
        if (startDate.equals(EnumUtilOutput.EMPTY.getValue())) {
            startDate = null;
        } else {
            try {
                LocalDate.parse(startDate);
            } catch (DateTimeParseException e) {
                throw new InvalidInputException("start_date");
            }
        }

        if (endDate.equals(EnumUtilOutput.EMPTY.getValue())) {
            endDate = null;
        } else {
            try {
                LocalDate.parse(endDate);
            } catch (DateTimeParseException e) {
                throw new InvalidInputException("end_date");
            }
        }

        // Check if the start or end dates or both are provided
        // The query must be different depending on what dates are provided
        // Create a blank Spec first and modify it depending on the provided dates
        Specification<Event> dateSpec = where(null);
        // If both dates are provided, build the full in between dates query
        if (startDate != null && endDate != null) {
            dateSpec = where(betweenDateLiterals(startDate, endDate))
                .or(betweenDateFields(startDate, endDate));
        } else if (startDate != null) {
            // Only the start date is provided
            // This uses the same query as with two dates but with the end date equal to the start date
            endDate = startDate;
            dateSpec = where(betweenDateLiterals(startDate, endDate))
                    .or(betweenDateFields(startDate, endDate));
        } else if (endDate != null) {
            // Only the end date is provided
            // This uses the same query as with two dates but with the start date equal to the end date
            startDate = endDate;
            dateSpec = where(betweenDateLiterals(startDate, endDate))
                    .or(betweenDateFields(startDate, endDate));
        }

        // Generate query for the event's technical details
        Specification<Event> technicalSpec = where(withId(eventFilterWrapper.getId()))
                .and(withMachine(eventFilterWrapper.getMachine()))
                .and(withSystem(eventFilterWrapper.getGlobal_prefix()))
                .and(withZone(eventFilterWrapper.getZone()));

        // Check the miscellaneous fields for null or blank values
        // Blank values must be converted to null to prevent the event specs from processing them
        // JIRA
        String jiraCase = stringUtil.checkInputTrimToUpper(eventFilterWrapper.getJira_case());
        if (jiraCase.equals(EnumUtilOutput.EMPTY.getValue())) jiraCase = null;
        // Features On
        String featuresOn = stringUtil.checkInputTrimToUpper(eventFilterWrapper.getFeatures_on());
        if (featuresOn.equals(EnumUtilOutput.EMPTY.getValue())) featuresOn = null;
        // Features Off
        String featuresOff = stringUtil.checkInputTrimToUpper(eventFilterWrapper.getFeatures_off());
        if (featuresOff.equals(EnumUtilOutput.EMPTY.getValue())) featuresOff = null;
        // Compiled sources
        String compiledSources = stringUtil.checkInputTrimToUpper(eventFilterWrapper.getCompiled_sources());
        if (compiledSources.equals(EnumUtilOutput.EMPTY.getValue())) compiledSources = null;
        // API used
        String apiUsed = stringUtil.checkInputTrimToUpper(eventFilterWrapper.getApi_used());
        if (apiUsed.equals(EnumUtilOutput.EMPTY.getValue())) apiUsed = null;
        // Generate query for the event's miscellaneous details
        Specification<Event> detailSpec = where(withJiraCase(jiraCase))
                .and(withFeaturesOn(featuresOn))
                .and(withFeaturesOff(featuresOff))
                .and(withCompiledSources(compiledSources))
                .and(withApiUsed(apiUsed));
                // Filters do not yet support users
//                .and(withCreatedBy(eventFilterWrapper.getCreated_by()))
//                .and(withLastChangedBy(eventFilterWrapper.getLast_modified_by()));

        // Generate query for the event types
        Specification<Event> eventTypeSpec = where(withEventTypeName(eventFilterWrapper.getEvent_type()))
                .or(withEventTypeCode(eventFilterWrapper.getEvent_type()));

        // Combine all queries in a final "AND" query
        Specification<Event> finalSpec = dateSpec
                .and(technicalSpec)
                .and(detailSpec)
                .and(eventTypeSpec);

        // Input the final query into the event DAO
        List<Event> events = eventDAO.findAll(finalSpec);

        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event: events) {
            eventDTOs.add(eventUtil.wrapEvent(event));
        }

        return eventDTOs;
    }

    @Override
    public List<EventDTO> searchEvents(String keyword) throws SystemException {
//        List<Event> events = eventDAO.search(keyword);
//        List<EventDTO> eventDTOs = new ArrayList<>();
//        for (Event event: events) {
//            eventDTOs.add(eventUtil.wrapEvent(event));
//        }
//
//        return eventDTOs;
        throw new SearchNotImplementedException();
    }
}
