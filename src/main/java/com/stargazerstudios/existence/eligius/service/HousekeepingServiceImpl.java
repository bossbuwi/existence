package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.conductor.erratum.file.FileCreationException;
import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;
import com.stargazerstudios.existence.eligius.config.FileStorageProperties;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import com.stargazerstudios.existence.eligius.utils.FileResponseUtil;
import com.stargazerstudios.existence.sonata.entity.Event;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class HousekeepingServiceImpl implements HousekeepingService {

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Autowired
    private FileResponseUtil fileResponseUtil;

    @Override
    public FileResponseDTO generateTemplate(String model) throws FileProcessingException {
        try {
            // This switch statement shall be updated when the app
            // has the capability to export other models
            switch (model.toUpperCase()) {
                case "EVENT" -> {
                    Path path = this.exportEventModelToSpreadsheet();
                    return fileResponseUtil.getFileDetails(path);
                }
                default -> throw new FileCreationException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileCreationException();
        }
    }

    @Override
    public Boolean buildEventsIndex() {
        return null;
    }

    private Path exportEventModelToSpreadsheet() throws FileProcessingException {
        // Try to create a new workbook
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create worksheet
            Sheet sheet = workbook.createSheet();
            // Get the column names for the new sheet
            // from the event model fields
            List<String> sheetColumns = new ArrayList<>();
            Field[] fields = Event.class.getDeclaredFields();
            for (Field field: fields) {
                Column col = field.getAnnotation(Column.class);
                if (col != null) {
                    sheetColumns.add(col.name());
                }
            }
            // Create the header row
            Row headerRow = sheet.createRow(0);
            // Put the column names based on the fields array
            for (int i = 0; i < sheetColumns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(sheetColumns.get(i));
            }
            // Create the filename
            String filename = "event_restore_template.xlsx";
            // Create the final path
            final String downloadDir = fileStorageProperties.getDownloadPath() + filename;
            // Create the file reference
            File file = new File(downloadDir);
            // Check if the complete path where the file is to be written exists
            boolean isExisting = file.exists();
            // TODO: This is a bit lacking.
            //  There are no means of validating that any of the succeeding processes
            //  (file deletion, file creation) has been successfully executed.
            if (isExisting) {
                // If existing, delete the file
                // Note that this could result in concurrency issues.
                file.delete();
                // Create the file
                file.createNewFile();
            } else {
                // If the file does not exist, create it directly
                file.createNewFile();
            }
            // Try to write the worksheet on the new file
            FileOutputStream outputStream = new FileOutputStream(file, false);
            workbook.write(outputStream);
            // Get the path of the file after it has been created
            String originalPath = file.getPath();
            // Create a Path object and return it
            return Paths.get(originalPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileCreationException();
        }
    }
}
