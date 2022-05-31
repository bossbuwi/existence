package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.EventDTO;
import com.stargazerstudios.existence.sonata.wrapper.EventWrapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class SheetImportServiceImpl implements SheetImportService {

    @Autowired
    private EventServiceImpl eventService;

    @Value("${file.import.path}")
    private String importPath;

    @Override
    public List<EventDTO> importSpreadSheet()
            throws IOException, DatabaseErrorException, UnknownInputException, EntityErrorException {
        List<EventDTO> eventList = new ArrayList<>();
        FileInputStream file = new FileInputStream(importPath);

        Workbook workbook = new XSSFWorkbook(file);
        int totalSheets = workbook.getNumberOfSheets();

        try {
            for (int i = 0; i < totalSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                int totalRows = sheet.getPhysicalNumberOfRows();
                for (int j = 1; j < totalRows; j++) {
                    Row row = sheet.getRow(j);
                    EventWrapper wrapper = createEventFromRow(row);
                    EventDTO event = eventService.createEvent(wrapper);
                    eventList.add(event);
                }
            }
        } catch (Exception e) {
            workbook.close();
            throw e;
        }

        workbook.close();
        return eventList;
    }

    private EventWrapper createEventFromRow(Row row) {
        EventWrapper wrapper = new EventWrapper();
        wrapper.set_imported(true);

        Cell cMachine = row.getCell(1);
        Cell cCreatedBy = row.getCell(2);
        Cell cLastModifiedBy = row.getCell(3);
        Cell cSystem = row.getCell(4);
        Cell cZones = row.getCell(5);
        Cell cEventTypes = row.getCell(6);
        Cell cJira = row.getCell(7);
        Cell cApi = row.getCell(8);
        Cell cSources = row.getCell(9);
        Cell cFeatureOn = row.getCell(10);
        Cell cFeatureOff = row.getCell(11);
        Cell cStartDate = row.getCell(12);
        Cell cEndDate = row.getCell(13);

        String machine = cMachine.getStringCellValue();
        wrapper.setMachine(machine);
        String globalPrefix = cSystem.getStringCellValue();
        wrapper.setGlobal_prefix(globalPrefix);

        String[] zones = cZones.getStringCellValue().split("-");
        wrapper.setZones(zones);

        String[] eTypeArr = cEventTypes.getStringCellValue().split("-");
        List<String> eTypeList = new ArrayList<>();
        for (String item: eTypeArr) {
            if (item.equals("MAINT")) {
                String replace = "SYSMAINT";
                eTypeList.add(replace);
            } else {
                eTypeList.add(item);
            }
        }
        String[] eventTypes = eTypeList.toArray(new String[eTypeList.size()]);
        wrapper.setEvent_types(eventTypes);

        LocalDate startDate = LocalDate.ofInstant(cStartDate.getDateCellValue().toInstant(), ZoneId.systemDefault());
        wrapper.setStart_date(startDate.toString());
        LocalDate endDate = LocalDate.ofInstant(cEndDate.getDateCellValue().toInstant(), ZoneId.systemDefault());
        wrapper.setEnd_date(endDate.toString());

        String createdBy = cCreatedBy.getStringCellValue();
        wrapper.setCreated_by(createdBy);
        String lastChangedBy = cLastModifiedBy.getStringCellValue();
        wrapper.setLast_modified_by(lastChangedBy);

        if (cJira != null) {
            String jiraCase = cJira.getStringCellValue();
            wrapper.setJira_case(jiraCase);
        }

        if (cApi != null) {
            String apiUsed = cApi.getStringCellValue();
            wrapper.setApi_used(apiUsed);
        }

        if (cSources != null) {
            String compiledSources = cSources.getStringCellValue();
            wrapper.setCompiled_sources(compiledSources);
        }

        if (cFeatureOn != null) {
            String featuresOn = cFeatureOn.getStringCellValue();
            wrapper.setFeatures_on(featuresOn);
        }

        if (cFeatureOff != null) {
            String featuresOff = cFeatureOff.getStringCellValue();
            wrapper.setFeatures_off(featuresOff);
        }

        return wrapper;
    }
}
