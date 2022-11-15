package com.stargazerstudios.existence.requiem.controller;

import com.stargazerstudios.existence.conductor.constants.SwitchableFeatures;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.erratum.system.InactiveSwitchableFeatureException;
import com.stargazerstudios.existence.requiem.dto.CoblogDTO;
import com.stargazerstudios.existence.requiem.service.CoblogServiceImpl;
import com.stargazerstudios.existence.requiem.wrapper.CoblogWrapper;
import com.stargazerstudios.existence.symphony.utils.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/requiem")
public class CoblogController {

    @Autowired
    private SettingUtil settingUtil;
    @Autowired
    private CoblogServiceImpl coblogService;

    /** Feature Dependency Check **/

    private boolean isRMQ001Active () {
        return settingUtil.isFeatureActive(SwitchableFeatures.RQM001.getValue());
    }

    /** Unguarded Endpoints **/

    @GetMapping("/con/coblogs/index")
    public ResponseEntity<List<CoblogDTO>> getAllCoblogs()
            throws SystemException {
        if (this.isRMQ001Active()) {
            return new ResponseEntity<>(coblogService.findAllCoblogs(), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.RQM001.getValue());
        }
    }

    /** Guarded Endpoints **/

    @PostMapping("/coblogs/coblog")
    public ResponseEntity<CoblogDTO> createCoblog(@RequestBody CoblogWrapper wrapper)
            throws DatabaseException, UnknownInputException, EntityException, SystemException {
        if (this.isRMQ001Active()) {
            return new ResponseEntity<>(coblogService.createCoblog(wrapper), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.RQM001.getValue());
        }
    }
}
