package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.conductor.erratum.root.FileProcessingException;
import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;


public interface SheetExportService {
    FileResponseDTO exportEventsToSpreadsheet() throws FileProcessingException;
}
