package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileProcessorService {
    void init();
    FileResponseDTO save(MultipartFile multipartFile) throws FileProcessingException;
    void deleteAll();
}
