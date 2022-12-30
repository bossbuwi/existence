package com.stargazerstudios.existence.requiem.controller;

import com.stargazerstudios.existence.conductor.constants.SwitchableFeatures;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemException;
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

    /** Feature Dependency Check **/

    private boolean isRMQ001Active () {
        return settingUtil.isFeatureActive(SwitchableFeatures.RQM001.getValue());
    }

    /** Unguarded Endpoints **/

    @GetMapping("/con/components/index")
    public ResponseEntity<List<ComponentDTO>> getAllComponents()
            throws SystemException {
        if (this.isRMQ001Active()) {
            return new ResponseEntity<>(componentService.getAllComponents(), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.RQM001.getValue());
        }
    }

    /** Guarded Endpoints **/

    @PostMapping("/components/component")
    public ResponseEntity<ComponentDTO> createComponent(@RequestBody ComponentWrapper wrapper)
            throws EntityException, DatabaseException, SystemException {
        if (this.isRMQ001Active()) {
            return new ResponseEntity<>(componentService.createComponent(wrapper), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.RQM001.getValue());
        }
    }
}
