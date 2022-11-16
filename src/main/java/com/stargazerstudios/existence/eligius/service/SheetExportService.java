package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.eligius.dto.FileResponseDTO;

import java.io.IOException;

public interface SheetExportService {
    FileResponseDTO exportEventsToSpreadsheet() throws IOException;
}
