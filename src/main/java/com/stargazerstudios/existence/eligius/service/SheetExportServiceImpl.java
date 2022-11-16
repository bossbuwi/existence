package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.eligius.config.FileStorageProperties;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;
import com.stargazerstudios.existence.sonata.entity.Event;
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
    private EventServiceImpl eventService;

    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Override
    public FileResponseDTO exportEventsToSpreadsheet() throws IOException {
        // Create new workbook
        Workbook workbook = new XSSFWorkbook();
        // Create new sheet within the workbook
        Sheet sheet = workbook.createSheet("Events");
        // Get the column names of the events table
        List<String> sheetColumns = new ArrayList<String>();
        Field[] fields = Event.class.getDeclaredFields();
        for (Field field : fields) {
            Column col = field.getAnnotation(Column.class);
            if (col != null) {
                sheetColumns.add(col.name());
                logger.info("Columns: " + col);
            }
        }
        // Create the header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < sheetColumns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(sheetColumns.get(i));
        }

        File file = new File("import/test.xlsx");
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file, false);
        workbook.write(outputStream);
        return null;
    }
}
