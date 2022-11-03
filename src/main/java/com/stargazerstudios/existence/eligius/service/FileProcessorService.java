package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileProcessorService {
    public void init();
    public FileResponseDTO save(MultipartFile multipartFile);
    public void deleteAll();
}
