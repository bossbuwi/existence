package com.stargazerstudios.existence.eligius.controller;

import com.stargazerstudios.existence.eligius.service.SearchServiceImpl;
import com.stargazerstudios.existence.eligius.wrapper.EventFilterWrapper;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/eligius")
public class SearchController {

    @Autowired
    private SearchServiceImpl searchService;

    /* Unguarded Endpoints*/

    @GetMapping("/con/filters/events")
    public ResponseEntity<List<EventDTO>> filterEvents(@RequestBody EventFilterWrapper eventFilterWrapper) {
        return new ResponseEntity<>(searchService.filterEvents(eventFilterWrapper), HttpStatus.OK);
    }

    @GetMapping("/con/search/events")
    public ResponseEntity<List<EventDTO>> searchEvents(@RequestParam String keyword) {
        return new ResponseEntity<>(searchService.searchEvents(keyword), HttpStatus.OK);
    }
}
