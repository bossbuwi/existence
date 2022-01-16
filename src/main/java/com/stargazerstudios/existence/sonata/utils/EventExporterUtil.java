package com.stargazerstudios.existence.sonata.utils;

import com.stargazerstudios.existence.sonata.entity.Event;
import com.stargazerstudios.existence.sonata.entity.EventType;
import com.stargazerstudios.existence.sonata.entity.Zone;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
public class EventExporterUtil {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public void exportToWorkbook(HttpServletResponse response, List<Event> events) throws IOException {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet();

        writeHeadings();
        writeData(events);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeHeadings() {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        createCell(headerRow, 0, "_id", headerStyle);
        createCell(headerRow, 1, "Start Date", headerStyle);
        createCell(headerRow, 2, "End Date", headerStyle);
        createCell(headerRow, 3, "Machine", headerStyle);
        createCell(headerRow, 4, "System", headerStyle);
        createCell(headerRow, 5, "Zones", headerStyle);
        createCell(headerRow, 6, "Event Types", headerStyle);
        createCell(headerRow, 7, "JIRA Case", headerStyle);
        createCell(headerRow, 8, "Features On", headerStyle);
        createCell(headerRow, 9, "Features Off", headerStyle);
        createCell(headerRow, 10, "Compiled Sources", headerStyle);
        createCell(headerRow, 11, "API Used", headerStyle);
        createCell(headerRow, 12, "Created By", headerStyle);
        createCell(headerRow, 13, "Created On", headerStyle);
        createCell(headerRow, 14, "Last Modified By", headerStyle);
        createCell(headerRow, 15, "Last Modified On", headerStyle);
    }

    private void writeData(List<Event> events) {
        if (events.size() > 0) {
            for (int i = 0; i < events.size(); i++) {
                Row dataRow = sheet.createRow(i + 1);
                CellStyle dataStyle = workbook.createCellStyle();

                // for id
                int id = (int) events.get(i).getId();
                // for start date
                LocalDate startDateO = events.get(i).getStartDate();
                String startDate = startDateO.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                // for end date
                LocalDate endDateO = events.get(i).getEndDate();
                String endDate = endDateO.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                // for machine
                String machine = events.get(i).getSystem().getMachine().getName();
                // for system
                String system = events.get(i).getSystem().getGlobalPrefix();
                // for zones
                Set<Zone> zoneSet = events.get(i).getZones();
                StringBuilder zonesBuilder = new StringBuilder();
                for (Zone zone: zoneSet) {
                    if (zonesBuilder.toString().equals("")) {
                        zonesBuilder = new StringBuilder(zone.getZonalPrefix());
                    } else {
                        zonesBuilder.append(",").append(zone.getZonalPrefix());
                    }
                }
                String zones = zonesBuilder.toString();
                // for event types
                Set<EventType> eventTypeSet = events.get(i).getEventTypes();
                StringBuilder eventTypesBuilder = new StringBuilder();
                for (EventType eventType: eventTypeSet) {
                    if (eventTypesBuilder.toString().equals("")) {
                        eventTypesBuilder = new StringBuilder(eventType.getCode());
                    } else {
                        eventTypesBuilder.append(",").append(eventType.getCode());
                    }
                }
                String eventTypes = eventTypesBuilder.toString();
                // for jira case
                String jiraCase = events.get(i).getJiraCase();
                // for features on
                String featuresOn = events.get(i).getFeaturesOn();
                // for features off
                String featuresOff = events.get(i).getFeaturesOff();
                // for compiled sources
                String compiledSources = events.get(i).getCompiledSources();
                // for api used
                String apiUsed = events.get(i).getApiUsed();
                // for created by
                String createdBy = events.get(i).getCreatedBy();
                // for creation date
                String createdOn = String.valueOf(events.get(i).getDateCreated());
                // for last modified by
                String lastModifiedBy = events.get(i).getLastChangedBy();
                // for edited date
                String modifiedOn = String.valueOf(events.get(i).getDateChanged());

                // write data on the corresponding cells
                createCell(dataRow, 0, id, dataStyle);
                createCell(dataRow, 1, startDate, dataStyle);
                createCell(dataRow, 2, endDate, dataStyle);
                createCell(dataRow, 3, machine, dataStyle);
                createCell(dataRow, 4, system, dataStyle);
                createCell(dataRow, 5, zones, dataStyle);
                createCell(dataRow, 6, eventTypes, dataStyle);
                createCell(dataRow, 7, jiraCase, dataStyle);
                createCell(dataRow, 8, featuresOn, dataStyle);
                createCell(dataRow, 9, featuresOff, dataStyle);
                createCell(dataRow, 10, compiledSources, dataStyle);
                createCell(dataRow, 11, apiUsed, dataStyle);
                createCell(dataRow, 12, createdBy, dataStyle);
                createCell(dataRow, 13, createdOn, dataStyle);
                createCell(dataRow, 14, lastModifiedBy, dataStyle);
                createCell(dataRow, 15, modifiedOn, dataStyle);
            }
        }
    }
}
