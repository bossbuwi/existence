package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemErrorException;
import com.stargazerstudios.existence.conductor.validation.groups.PutValidation;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.service.SettingServiceImpl;
import com.stargazerstudios.existence.symphony.wrapper.SettingWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/symphony")
public class SettingController {

    @Autowired
    private SettingServiceImpl settingService;

    @GetMapping("/settings/index")
    public ResponseEntity<List<SettingDTO>> getAllSettings() {
        return new ResponseEntity<>(settingService.getAllSettings(), HttpStatus.OK);
    }

    @GetMapping("/settings/setting/switchable")
    public ResponseEntity<List<SettingDTO>> getAllSwitchableFeatures() {
        return new ResponseEntity<>(settingService.getAllSwitchableFeatures(), HttpStatus.OK);
    }

    @GetMapping("/settings/setting/backend")
    public ResponseEntity<List<SettingDTO>> getAllBackendFeatures() {
        return new ResponseEntity<>(settingService.getAllBackendFeatures(), HttpStatus.OK);
    }

    @GetMapping("/settings/setting/frontend")
    public ResponseEntity<List<SettingDTO>> getAllFrontendFeatures() {
        return new ResponseEntity<>(settingService.getAllFrontendFeatures(), HttpStatus.OK);
    }

    @GetMapping("/settings/setting/{id}")
    public ResponseEntity<SettingDTO> getSettingById(@Min(1) @PathVariable("id") long id)
            throws EntityErrorException {
        return new ResponseEntity<>(settingService.getSettingById(id), HttpStatus.OK);
    }

    @PutMapping("/settings/setting")
    public ResponseEntity<SettingDTO> modifySetting(@Validated(PutValidation.class)
                                                        @RequestBody SettingWrapper setting)
            throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException, SystemErrorException {
        return new ResponseEntity<>(settingService.modifySetting(setting), HttpStatus.OK);
    }
}
