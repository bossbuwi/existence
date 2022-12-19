package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.conductor.erratum.file.FileCreationException;
import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;
import com.stargazerstudios.existence.eligius.config.FileStorageProperties;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import com.stargazerstudios.existence.eligius.utils.FileResponseUtil;
import com.stargazerstudios.existence.sonata.entity.Event;
import com.stargazerstudios.existence.sonata.entity.EventType;
import com.stargazerstudios.existence.sonata.entity.System;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.repository.EventDAO;
import com.stargazerstudios.existence.sonata.repository.SystemDAO;
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
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class SheetExportServiceImpl implements SheetExportService {

    private static final Logger logger = LoggerFactory.getLogger(SheetExportServiceImpl.class);

    @Autowired
    private SystemDAO systemDAO;

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private FileResponseUtil fileResponseUtil;

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
                    // Add the event's id
                    Cell eId = row.createCell(0);
                    eId.setCellValue(events.get(j).getId());
                    // Add the event's start date
                    Cell eStartDate = row.createCell(1);
                    eStartDate.setCellValue(events.get(j).getStartDate().toString());
                    // Add the event's end date
                    Cell eEndDate = row.createCell(2);
                    eEndDate.setCellValue(events.get(j).getEndDate().toString());
                    // Add the event's jira case
                    Cell eJiraCase = row.createCell(3);
                    eJiraCase.setCellValue(events.get(j).getJiraCase());
                    // Add the event's features on
                    Cell eFeatureOn = row.createCell(4);
                    eFeatureOn.setCellValue(events.get(j).getFeaturesOn());
                    // Add the event's features off
                    Cell eFeatureOff = row.createCell(5);
                    eFeatureOff.setCellValue(events.get(j).getFeaturesOff());
                    // Add the event's compiled sources
                    Cell eCompiledSources = row.createCell(6);
                    eCompiledSources.setCellValue(events.get(j).getCompiledSources());
                    // Add the event's api used
                    Cell eApiUsed = row.createCell(7);
                    eApiUsed.setCellValue(events.get(j).getApiUsed());
                    // Add the event's created by
                    Cell eCreatedBy = row.createCell(8);
                    eCreatedBy.setCellValue(events.get(j).getCreatedBy());
                    // Add the event's creation date
                    Cell eCreationDate = row.createCell(9);
                    eCreationDate.setCellValue(events.get(j).getDateCreated().toString());
                    // Add the event's last changed by
                    Cell eLastChangedBy = row.createCell(10);
                    eLastChangedBy.setCellValue(events.get(j).getLastChangedBy());
                    // Add the event's last changed date
                    Cell eLastChangedDate = row.createCell(11);
                    eLastChangedDate.setCellValue(events.get(j).getDateChanged().toString());
                    // Add the event's machine
                    Cell eMachine = row.createCell(12);
                    eMachine.setCellValue(events.get(j).getSystem().getMachine().getName());
                    // Add the event's system
                    Cell eGlobalPrefix = row.createCell(13);
                    eGlobalPrefix.setCellValue(events.get(j).getSystem().getGlobalPrefix());
                    // Add the event's zones
                    Cell eZones = row.createCell(14);
                    ArrayList<Zone> zones = new ArrayList<>(events.get(j).getZones());
                    String zoneStr = zones.stream().map(Zone::getZonalPrefix).collect(Collectors.joining(","));
                    eZones.setCellValue(zoneStr);
                    // Add the event's event types
                    Cell eEventTypes = row.createCell(15);
                    ArrayList<EventType> eventTypes = new ArrayList<>(events.get(j).getEventTypes());
                    String eventTypeStr = eventTypes.stream().map(EventType::getCode).collect(Collectors.joining(","));
                    eEventTypes.setCellValue(eventTypeStr);
                }
            }

            // Build the filename and the directory where it will be created
            String filename = "backup_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) +
                    ".xlsx";
            final String downloadDir = fileStorageProperties.getDownloadPath() + filename;
            // Try to create file
            File file = new File(downloadDir);
            boolean isCreated = file.createNewFile();
            if (isCreated) {
                String originalPath = file.getPath();
                FileOutputStream outputStream = new FileOutputStream(file, false);
                // Write the workbook contents to the newly created file
                workbook.write(outputStream);
                // Return the path where the file is located
                return fileResponseUtil.getFileDetails(Paths.get(originalPath));
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileCreationException();
        }
    }
}
