package com.stargazerstudios.existence.wayfarer.controller;

import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.wayfarer.service.DevelopmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/wayfarer")
public class DevelopmentController {

    @Autowired
    private DevelopmentServiceImpl developmentService;

    @GetMapping("/get/001")
    public ResponseEntity<List<SystemDTO>> getSystemsWithEvents() {
        return new ResponseEntity<>(developmentService.getSystemsWithEvents(), HttpStatus.OK);
    }

    @GetMapping("/get/002")
    public ResponseEntity<List<EventDTO>> getEventsOnSystem() {
//        return new ResponseEntity<>(developmentService.getEventsOnSystem(), HttpStatus.OK);
        return null;
    }
}
