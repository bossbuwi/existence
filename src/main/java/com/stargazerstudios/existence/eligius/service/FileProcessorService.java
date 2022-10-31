package com.stargazerstudios.existence.eligius.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessorService {
    public void init();
    public boolean save(MultipartFile multipartFile);
    public void deleteAll();
}
