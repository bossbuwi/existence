package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.validation.groups.PostValidation;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.service.EventServiceImpl;
import com.stargazerstudios.existence.eligius.service.SheetImportServiceImpl;
import com.stargazerstudios.existence.sonata.utils.EventExporterUtil;
import com.stargazerstudios.existence.sonata.wrapper.EventWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
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

    @Autowired
    private SheetImportServiceImpl importService;

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
            throws UnknownInputException, EntityException, DatabaseException {
        return new ResponseEntity<>(eventService.createEvent(event), HttpStatus.OK);
    }

    @PutMapping("/events/event")
    public ResponseEntity<EventDTO> updateEvent(@Validated(PutValidation.class)
                                                    @RequestBody EventWrapper event)
            throws UnknownInputException, EntityException, DatabaseException, AuthorizationException {
        return new ResponseEntity<>(eventService.updateEvent(event), HttpStatus.OK);
    }

    @DeleteMapping("/events/event/{id}")
    public ResponseEntity<Boolean> deleteEvent(@NotBlank @PathVariable("id") long id)
            throws DatabaseException, EntityException, AuthorizationException {
        return new ResponseEntity<>(eventService.deleteEvent(id), HttpStatus.OK);
    }

    @GetMapping("/events/export")
    public void exportEventsToWorkbook(HttpServletResponse response) throws IOException {
        eventService.exportEvents(response);
    }

//    @PostMapping("/events/import")
//    public ResponseEntity<List<EventDTO>> importEventsFromWorkbook(@RequestParam("filename") String filename)
//            throws DatabaseErrorException, UnknownInputException, IOException, EntityErrorException {
//        return new ResponseEntity<>(importService.importSpreadSheet(filename), HttpStatus.OK);
//    }
}
