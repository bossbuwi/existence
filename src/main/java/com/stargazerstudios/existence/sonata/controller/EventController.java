package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.service.EventServiceImpl;
import com.stargazerstudios.existence.sonata.utils.EventExporterUtil;
import com.stargazerstudios.existence.sonata.wrapper.EventWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sonata")
public class EventController {

    @Autowired
    private EventServiceImpl eventService;

    @Autowired
    private EventExporterUtil eventExporterUtil;

    /* Unguarded Endpoints */
    @GetMapping("/con/events/index")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/con/events/date")
    public ResponseEntity<List<EventDTO>> getEventsOnDate(@RequestParam String date)
            throws UnknownInputException {
        return new ResponseEntity<>(eventService.getEventsByDate(date), HttpStatus.OK);
    }

    @GetMapping("/con/events/date/range")
    public ResponseEntity<List<EventDTO>> getEventsBetweenDates(@RequestParam String start, @RequestParam String end)
            throws UnknownInputException {
        return new ResponseEntity<>(eventService.getEventsBetweenDates(start, end), HttpStatus.OK);
    }

    @GetMapping("/con/events/latest")
    public ResponseEntity<EventDTO> getLatestEvent() {
        return new ResponseEntity<>(eventService.getLatestEvent(), HttpStatus.OK);
    }

    @GetMapping("/con/events/count")
    public ResponseEntity<Long> getEventCount() {
        return new ResponseEntity<>(eventService.getEventCount(), HttpStatus.OK);
    }

    /* Guarded Endpoints */
    @PostMapping("/events/event")
    public ResponseEntity<EventDTO> createEvent(@Validated(PostValidation.class)
                                                    @RequestBody EventWrapper event)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException {
        return new ResponseEntity<>(eventService.createEvent(event), HttpStatus.OK);
    }

    @PutMapping("/events/event")
    public ResponseEntity<EventDTO> updateEvent(@Validated(PutValidation.class)
                                                    @RequestBody EventWrapper event)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException, AuthorizationErrorException {
        return new ResponseEntity<>(eventService.updateEvent(event), HttpStatus.OK);
    }

    @GetMapping("/events/export")
    public void exportEventsToWorkbook(HttpServletResponse response) throws IOException {
        eventService.exportEvents(response);
    }
}
