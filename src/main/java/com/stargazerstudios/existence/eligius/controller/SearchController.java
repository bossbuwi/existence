package com.stargazerstudios.existence.eligius.controller;

import com.stargazerstudios.existence.conductor.constants.SwitchableFeatures;
import com.stargazerstudios.existence.conductor.erratum.root.SystemException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.erratum.system.InactiveSwitchableFeatureException;
import com.stargazerstudios.existence.eligius.service.SearchServiceImpl;
import com.stargazerstudios.existence.eligius.wrapper.EventFilterWrapper;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.symphony.utils.SettingUtil;
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
    private SettingUtil settingUtil;

    @Autowired
    private SearchServiceImpl searchService;

    /** Feature Dependency Check **/

    private boolean isELS001Active () {
        return settingUtil.isFeatureActive(SwitchableFeatures.ELS001.getValue());
    }

    /* Unguarded Endpoints*/

    @PostMapping("/con/filters/events")
    public ResponseEntity<List<EventDTO>> filterEvents(@RequestBody EventFilterWrapper eventFilterWrapper)
            throws SystemException, UnknownInputException {
        if (isELS001Active()) {
            return new ResponseEntity<>(searchService.filterEvents(eventFilterWrapper), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.ELS001.getValue());
        }
    }

    @GetMapping("/con/search/events")
    public ResponseEntity<List<EventDTO>> searchEvents(@RequestParam String keyword)
            throws SystemException {
        if (isELS001Active()) {
            return new ResponseEntity<>(searchService.searchEvents(keyword), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.ELS001.getValue());
        }
    }
}
