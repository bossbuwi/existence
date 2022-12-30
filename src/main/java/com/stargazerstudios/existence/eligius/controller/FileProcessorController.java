package com.stargazerstudios.existence.eligius.controller;

import com.stargazerstudios.existence.conductor.constants.SwitchableFeatures;
import com.stargazerstudios.existence.conductor.erratum.root.*;
import com.stargazerstudios.existence.conductor.erratum.system.InactiveSwitchableFeatureException;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import com.stargazerstudios.existence.eligius.service.FileProcessorServiceImpl;
import com.stargazerstudios.existence.symphony.utils.SettingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/eligius")
public class FileProcessorController {

    @Autowired
    private FileProcessorServiceImpl fileProcessorService;

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

    @GetMapping("/files/download")
    public ResponseEntity<?> downloadFile(@RequestParam("filename") String filename)
            throws FileProcessingException, SystemException {
        if (isELS002Active()) {
            String headerValue = "attachment; filename=" + filename;
            ByteArrayResource resource = fileProcessorService.send(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);
        } else {
            throw new InactiveSwitchableFeatureException(SwitchableFeatures.ELS002.getValue());
        }
    }
}
