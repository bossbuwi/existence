package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.eligius.config.FileStorageProperties;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class FileProcessorServiceImpl implements FileProcessorService {

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Override
    public void init() {
        final Path root = Paths.get(fileStorageProperties.getUploadPath());
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public boolean save(MultipartFile multipartFile) {
        // TODO: This should return a custom DTO.
        final Path root = Paths.get(fileStorageProperties.getUploadPath());
        try {
            Path originalPath = root.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            String pathName = originalPath.toString();
            String extension = FilenameUtils.getExtension(pathName);

            if (extension.equalsIgnoreCase("xlsx") || extension.equalsIgnoreCase("xls")) {
                String newFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"))
                        + "."
                        + extension;
                Path newPath = root.resolve(newFileName);
                Files.copy(multipartFile.getInputStream(), newPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO: This must throw a custom error.
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return true;
    }

    @Override
    public void deleteAll() {
        final Path root = Paths.get(fileStorageProperties.getUploadPath());
        FileSystemUtils.deleteRecursively(root.toFile());
    }
}
