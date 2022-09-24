package com.stargazerstudios.existence.requiem.controller;

import com.stargazerstudios.existence.conductor.constants.SwitchableFeatures;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.SystemErrorException;
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

    /** Unguarded Endpoints **/

    @GetMapping("/con/coblogs/index")
    public ResponseEntity<List<CoblogDTO>> getAllCoblogs()
            throws SystemErrorException {
        if (settingUtil.isFeatureActive(SwitchableFeatures.RQM001.getValue())) {
            return new ResponseEntity<>(coblogService.findAllCoblogs(), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.RQM001.getValue());
        }
    }

    /** Guarded Endpoints **/

    @PostMapping("/coblogs/coblog")
    public ResponseEntity<CoblogDTO> createCoblog(@RequestBody CoblogWrapper wrapper)
            throws DatabaseErrorException, UnknownInputException, EntityErrorException, SystemErrorException {
        if (settingUtil.isFeatureActive(SwitchableFeatures.RQM001.getValue())) {
            return new ResponseEntity<>(coblogService.createCoblog(wrapper), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.RQM001.getValue());
        }
    }
}
