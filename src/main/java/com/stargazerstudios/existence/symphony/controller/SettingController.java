package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.BadJsonWebTokenException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.service.SettingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/metaverse/midas/settings")
public class SettingController {

    @Autowired
    SettingServiceImpl settingService;

    @GetMapping("/index")
    public ResponseEntity<List<SettingDTO>> getAllSettings() {
        return new ResponseEntity<>(settingService.getAllSettings(), HttpStatus.OK);
    }

    @GetMapping("/setting/{id}")
    public ResponseEntity<SettingDTO> getSettingById(@PathVariable("id") long id)
            throws BadJsonWebTokenException, EntityNotFoundException {
        return new ResponseEntity<>(settingService.getSettingById(id), HttpStatus.OK);
    }

    @PostMapping("/setting/{id}")
    public ResponseEntity<SettingDTO> modifySetting(@PathVariable("id") long id) {
        return null;
    }
}
