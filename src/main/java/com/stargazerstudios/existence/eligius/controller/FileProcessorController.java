package com.stargazerstudios.existence.eligius.controller;

import com.stargazerstudios.existence.conductor.constants.SwitchableFeatures;
import com.stargazerstudios.existence.conductor.erratum.file.FileCreationException;
import com.stargazerstudios.existence.conductor.erratum.root.*;
import com.stargazerstudios.existence.conductor.erratum.system.InactiveSwitchableFeatureException;
import com.stargazerstudios.existence.eligius.config.FileStorageProperties;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import com.stargazerstudios.existence.eligius.service.FileProcessorServiceImpl;
import com.stargazerstudios.existence.eligius.service.SheetExportServiceImpl;
import com.stargazerstudios.existence.eligius.service.SheetImportServiceImpl;
import com.stargazerstudios.existence.eligius.utils.FileResponseUtil;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.symphony.utils.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/eligius")
public class FileProcessorController {

    @Autowired
    private FileProcessorServiceImpl fileProcessorService;

    @Autowired
    private SheetImportServiceImpl sheetImportService;

    @Autowired
    private SheetExportServiceImpl sheetExportService;

    @Autowired
    private FileResponseUtil fileResponseUtil;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Autowired
    private SettingUtil settingUtil;

    /** Feature Dependency Check **/

    private boolean isELS002Active () {
        return settingUtil.isFeatureActive(SwitchableFeatures.ELS002.getValue());
    }

    /* Guarded Endpoints */

    @PostMapping("/files/upload")
    public ResponseEntity<FileResponseDTO> uploadFile(@RequestParam("file") MultipartFile file)
            throws FileProcessingException, SystemException {
        if (isELS002Active()) {
            return new ResponseEntity<>(fileProcessorService.save(file), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.ELS002.getValue());
        }
    }

    @PostMapping("files/restore/event")
    public ResponseEntity<List<EventDTO>> importEventsFromSpreadsheet(@RequestParam("filename") String filename)
            throws DatabaseException, UnknownInputException, IOException, EntityException, SystemException {
        if (isELS002Active()) {
            return new ResponseEntity<>(sheetImportService.importEventsFromSpreadsheet(filename), HttpStatus.OK);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.ELS002.getValue());
        }
    }

    @GetMapping("files/backup/download")
    public ResponseEntity<?> downloadFile(@RequestParam("filename") String filename)
            throws FileProcessingException {
        // TODO: This needs to validate if the file is indeed existing on the server
        //  It is not a problem if reality is used but will be a possible problem
        //  on other API consumers. It also needs to respect a switchable feature (ELS002).
        String headerValue = "attachment; filename=" + filename;
        ByteArrayResource resource = fileProcessorService.send(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    @GetMapping("files/backup/event")
    public ResponseEntity<FileResponseDTO> exportEventsToSpreadsheet()
            throws FileProcessingException {
        return new ResponseEntity<>(sheetExportService.exportEventsToSpreadsheet(), HttpStatus.OK);
    }
}
