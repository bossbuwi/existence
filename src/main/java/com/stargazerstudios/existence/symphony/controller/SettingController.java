package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.service.SettingServiceImpl;
import com.stargazerstudios.existence.symphony.wrapper.SettingWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/symphony/settings")
public class SettingController {

    @Autowired
    private SettingServiceImpl settingService;

    @GetMapping("/index")
    public ResponseEntity<List<SettingDTO>> getAllSettings() {
        return new ResponseEntity<>(settingService.getAllSettings(), HttpStatus.OK);
    }

    @GetMapping("/setting/{type}")
    public ResponseEntity<List<SettingDTO>> getSettingsByType(@PathVariable("type") String type) throws EntityErrorException {
        return new ResponseEntity<>(settingService.getSettingsByType(type), HttpStatus.OK);
    }

    @GetMapping("/setting/{id}")
    public ResponseEntity<SettingDTO> getSettingById(@PathVariable("id") long id) throws EntityErrorException {
        return new ResponseEntity<>(settingService.getSettingById(id), HttpStatus.OK);
    }

    @PutMapping("/setting")
    public ResponseEntity<SettingDTO> modifySetting(@RequestBody SettingWrapper settingWrapper) throws EntityErrorException {
        return new ResponseEntity<>(settingService.modifySetting(settingWrapper), HttpStatus.OK);
    }
}
