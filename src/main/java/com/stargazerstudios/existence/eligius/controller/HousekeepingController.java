package com.stargazerstudios.existence.eligius.controller;

import com.stargazerstudios.existence.conductor.constants.SwitchableFeatures;
import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemException;
import com.stargazerstudios.existence.conductor.erratum.system.InactiveSwitchableFeatureException;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import com.stargazerstudios.existence.eligius.service.HousekeepingServiceImpl;
import com.stargazerstudios.existence.symphony.utils.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/eligius")
public class HousekeepingController {

    @Autowired
    private HousekeepingServiceImpl housekeepingService;

    @Autowired
    private SettingUtil settingUtil;

    private boolean isELS002Active () {
        return settingUtil.isFeatureActive(SwitchableFeatures.ELS002.getValue());
    }

    @PostMapping("/housekeeping/template/generate")
    public ResponseEntity<FileResponseDTO> generateTemplate(@RequestParam("model") String model)
            throws FileProcessingException, SystemException {
        if (isELS002Active()) {
            return new ResponseEntity<>(housekeepingService.generateTemplate(model), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.ELS002.getValue());
        }
    }
}
