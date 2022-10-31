package com.stargazerstudios.existence.eligius.controller;

import com.stargazerstudios.existence.eligius.service.FileProcessorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/eligius")
public class RestoreController {

    @Autowired
    private FileProcessorServiceImpl fileProcessorService;

    @PostMapping("/files/upload")
    public ResponseEntity<Boolean> uploadFile(@RequestParam("file")MultipartFile file) {
        return new ResponseEntity<>(fileProcessorService.save(file), HttpStatus.OK);
    }
}
