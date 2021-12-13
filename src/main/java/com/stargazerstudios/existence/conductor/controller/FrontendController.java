package com.stargazerstudios.existence.conductor.controller;

import com.stargazerstudios.existence.conductor.constants.FrontendInit;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.model.ExistenceIdentity;
import com.stargazerstudios.existence.conductor.service.FrontendService;
import com.stargazerstudios.existence.symphony.dto.SettingDTO;
import com.stargazerstudios.existence.symphony.service.SettingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/conductor/")
public class FrontendController {

    @Autowired
    private FrontendService frontendService;

    @Autowired
    private SettingServiceImpl settingService;

    @GetMapping("/loglevel")
    public ResponseEntity<SettingDTO> getAllSettings() throws EntityNotFoundException {
        return new ResponseEntity<>(settingService.getSettingByKey(FrontendInit.FRONTEND_LOGGING), HttpStatus.OK);
    }

    @GetMapping("/init")
    public ResponseEntity<ExistenceIdentity> initClient() {
        return new ResponseEntity<>(frontendService.getAppIdentity(), HttpStatus.OK);
    }
}
