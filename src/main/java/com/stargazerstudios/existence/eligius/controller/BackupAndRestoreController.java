package com.stargazerstudios.existence.eligius.controller;

import com.stargazerstudios.existence.conductor.constants.SwitchableFeatures;
import com.stargazerstudios.existence.conductor.erratum.root.*;
import com.stargazerstudios.existence.conductor.erratum.system.InactiveSwitchableFeatureException;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import com.stargazerstudios.existence.eligius.service.SheetExportServiceImpl;
import com.stargazerstudios.existence.eligius.service.SheetImportServiceImpl;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.symphony.utils.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/eligius")
public class BackupAndRestoreController {

    @Autowired
    private SheetImportServiceImpl sheetImportService;

    @Autowired
    private SheetExportServiceImpl sheetExportService;

    @Autowired
    private SettingUtil settingUtil;

    /* Feature Dependency Check */

    private boolean isELS002Active () {
        return settingUtil.isFeatureActive(SwitchableFeatures.ELS002.getValue());
    }

    /* Guarded Endpoints */

    @PostMapping("/records/restore/event")
    public ResponseEntity<List<EventDTO>> importEventsFromSpreadsheet(@RequestParam("filename") String filename)
            throws DatabaseException, UnknownInputException, IOException, EntityException, SystemException {
        if (isELS002Active()) {
            return new ResponseEntity<>(sheetImportService.importEventsFromSpreadsheet(filename), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.ELS002.getValue());
        }
    }

    @PostMapping("/records/backup/event")
    public ResponseEntity<FileResponseDTO> exportEventsToSpreadsheet()
            throws FileProcessingException, SystemException {
        if (isELS002Active()) {
            return new ResponseEntity<>(sheetExportService.exportEventsToSpreadsheet(), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.ELS002.getValue());
        }
    }
}
