package com.stargazerstudios.existence.sonata.controller;

import com.stargazerstudios.existence.sonata.dto.EventTypeDTO;
import com.stargazerstudios.existence.sonata.service.EventTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/sonata")
public class EventTypeController {

    @Autowired
    private EventTypeServiceImpl eventTypeService;

    /* Unguarded Endpoints */
    @GetMapping("/con/event-types/index")
    public ResponseEntity<List<EventTypeDTO>> getAllEvents() {
        return new ResponseEntity<>(eventTypeService.getAllEvents(), HttpStatus.OK);
    }
}
