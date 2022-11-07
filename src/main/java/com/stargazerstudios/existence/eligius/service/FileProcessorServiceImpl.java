package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.conductor.erratum.file.FileUploadException;
import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;
import com.stargazerstudios.existence.eligius.config.FileStorageProperties;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import com.stargazerstudios.existence.eligius.utils.FileResponseUtil;
import org.apache.commons.io.FilenameUtils;
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

    @Autowired
    private FileResponseUtil fileResponseUtil;

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
    public FileResponseDTO save(MultipartFile multipartFile) throws FileProcessingException {
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
                return fileResponseUtil.getFileDetails(newPath);
            } else {
                throw new FileUploadException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileUploadException();
        }
    }

    @Override
    public void deleteAll() {
        final Path root = Paths.get(fileStorageProperties.getUploadPath());
        FileSystemUtils.deleteRecursively(root.toFile());
    }
}
