package com.stargazerstudios.existence.eligius.utils;

import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileResponseUtil {

    public FileResponseDTO getFileDetails(Path path) throws IOException {
        // TODO: The IOException was not handled properly.
        if (Files.exists(path)) {
            String pathName = path.toString();
            String filename = FilenameUtils.getName(pathName);
            String extension = FilenameUtils.getExtension(pathName);
            long size = Files.size(path);

            FileResponseDTO fileResponseDTO = new FileResponseDTO();
            fileResponseDTO.setFilename(filename);
            fileResponseDTO.setExtension(extension);
            fileResponseDTO.setSize(size);
            fileResponseDTO.setSaved(true);

            return fileResponseDTO;
        }

        return null;
    }
}
