package com.stargazerstudios.existence.requiem.controller;

import com.stargazerstudios.existence.conductor.constants.SwitchableFeatures;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemErrorException;
import com.stargazerstudios.existence.conductor.erratum.system.InactiveSwitchableFeatureException;
import com.stargazerstudios.existence.requiem.dto.ComponentDTO;
import com.stargazerstudios.existence.requiem.service.ComponentServiceImpl;
import com.stargazerstudios.existence.requiem.wrapper.ComponentWrapper;
import com.stargazerstudios.existence.symphony.utils.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/requiem")
public class ComponentController {

    @Autowired
    private SettingUtil settingUtil;

    @Autowired
    private ComponentServiceImpl componentService;

    /** Unguarded Endpoints **/

    @GetMapping("/con/components/index")
    public ResponseEntity<List<ComponentDTO>> getAllComponents()
            throws SystemErrorException {
        if (settingUtil.isFeatureActive(SwitchableFeatures.RQM001.getValue())) {
            return new ResponseEntity<>(componentService.getAllComponents(), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.RQM001.getValue());
        }
    }

    /** Guarded Endpoints **/

    @PostMapping("/components/component")
    public ResponseEntity<ComponentDTO> createComponent(@RequestBody ComponentWrapper wrapper)
            throws EntityErrorException, DatabaseErrorException, SystemErrorException {
        if (settingUtil.isFeatureActive(SwitchableFeatures.RQM001.getValue())) {
            return new ResponseEntity<>(componentService.createComponent(wrapper), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.RQM001.getValue());
        }
    }
}
