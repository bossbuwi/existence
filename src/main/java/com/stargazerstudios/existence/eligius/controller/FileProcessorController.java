package com.stargazerstudios.existence.eligius.controller;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import com.stargazerstudios.existence.eligius.service.FileProcessorServiceImpl;
import com.stargazerstudios.existence.eligius.service.SheetImportServiceImpl;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/eligius")
public class FileProcessorController {

    @Autowired
    private FileProcessorServiceImpl fileProcessorService;

    @Autowired
    private SheetImportServiceImpl sheetImportService;

    /* Guarded Endpoints */

    @PostMapping("/files/upload")
    public ResponseEntity<FileResponseDTO> uploadFile(@RequestParam("file") MultipartFile file)
            throws FileProcessingException {
        return new ResponseEntity<>(fileProcessorService.save(file), HttpStatus.OK);
    }

    @PostMapping("files/restore/event")
    public ResponseEntity<List<EventDTO>> importEventsFromSpreadsheet(@RequestParam("filename") String filename)
            throws DatabaseErrorException, UnknownInputException, IOException, EntityErrorException {
        return new ResponseEntity<>(sheetImportService.importEventsFromSpreadsheet(filename), HttpStatus.OK);
    }
}
