package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.conductor.erratum.file.FileCreationException;
import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;
import com.stargazerstudios.existence.eligius.config.FileStorageProperties;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import com.stargazerstudios.existence.sonata.entity.Event;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.repository.EventDAO;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
import com.stargazerstudios.existence.sonata.service.EventServiceImpl;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SheetExportServiceImpl implements SheetExportService {

    private static final Logger logger = LoggerFactory.getLogger(SheetExportServiceImpl.class);

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Override
    public FileResponseDTO exportEventsToSpreadsheet() throws FileProcessingException {
        // Try to create new workbook
        try (Workbook workbook = new XSSFWorkbook()){
            // Get the number of systems that have events
            List<System> systems = systemDAO.findSystemsWithEvents();
            // Create sheets based on the number of systems that have events
            for (System system: systems) {
                // Get the system's global prefix and use it as the sheet name
                String sheetName = system.getGlobalPrefix() + " System";
                // Create new sheet within the workbook
                Sheet sheet = workbook.createSheet(sheetName);
                // Get the column names of the events table
                List<String> sheetColumns = new ArrayList<>();
                Field[] fields = Event.class.getDeclaredFields();
                for (Field field : fields) {
                    Column col = field.getAnnotation(Column.class);
                    if (col != null) {
                        sheetColumns.add(col.name());
//                        logger.info("Columns: " + col);
                    }
                }
                // Create the header row
                Row headerRow = sheet.createRow(0);
                // Add column names from the events table column names
                for (int i = 0; i < sheetColumns.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(sheetColumns.get(i));
                }
                // Add machine column
                Cell machineCell = headerRow.createCell(sheetColumns.size());
                machineCell.setCellValue("machine");
                // Add system column
                Cell systemCell = headerRow.createCell(sheetColumns.size() + 1);
                systemCell.setCellValue("system");
                // Add zone column
                Cell zoneCell = headerRow.createCell(sheetColumns.size() + 2);
                zoneCell.setCellValue("zone");
                // Add event type column
                Cell eventTypeCell = headerRow.createCell(sheetColumns.size() + 3);
                eventTypeCell.setCellValue("event_type");

                // Get the system's events
                List<Event> events = eventDAO.findEventsOnSystem(system.getGlobalPrefix());
                // Loop through the list and a row for each event found
                for (int j = 0; j < events.size(); j++) {
                    // Row number is always loop iterator + 1 because the header is the zeroth row
                    int rowNumber = j + 1;
                    Row row = sheet.createRow(rowNumber);
                    // Create each cell and manually fill it with details based on the header column name
                }
            }

            File file = new File("import/test.xlsx");
            boolean isCreated = file.createNewFile();
            if (isCreated) {
                FileOutputStream outputStream = new FileOutputStream(file, false);
                workbook.write(outputStream);
                return null;
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            throw new FileCreationException();
        }
    }
}
