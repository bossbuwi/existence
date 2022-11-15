package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.EventDTO;

import java.io.IOException;
import java.util.List;

public interface SheetImportService {
    List<EventDTO> importEventsFromSpreadsheet(String filename)
            throws IOException, DatabaseException, UnknownInputException, EntityException;
}
