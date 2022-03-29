package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.service.EventServiceImpl;
import com.stargazerstudios.existence.sonata.utils.EventExporterUtil;
import com.stargazerstudios.existence.sonata.wrapper.EventWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sonata/events")
public class EventController {

    @Autowired
    private EventServiceImpl eventService;

    @Autowired
    private EventExporterUtil eventExporterUtil;

    @GetMapping("/index")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/date")
    public ResponseEntity<List<EventDTO>> getEventsOnDate(@RequestParam String date)
            throws UnknownInputException {
        return new ResponseEntity<>(eventService.getEventsByDate(date), HttpStatus.OK);
    }

    @PostMapping("/event")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventWrapper event)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException {
        return new ResponseEntity<>(eventService.createEvent(event), HttpStatus.OK);
    }

    @PutMapping("/event")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventWrapper event)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException, AuthorizationErrorException {
        return new ResponseEntity<>(eventService.updateEvent(event), HttpStatus.OK);
    }

    @GetMapping("/export")
    public void exportEventsToWorkbook(HttpServletResponse response) throws IOException {
        eventService.exportEvents(response);
    }
}
