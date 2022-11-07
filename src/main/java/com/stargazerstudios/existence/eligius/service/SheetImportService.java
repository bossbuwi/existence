package com.stargazerstudios.existence.eligius.service;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.EventDTO;

import java.io.IOException;
import java.util.List;

public interface SheetImportService {
    List<EventDTO> importEventsFromSpreadsheet(String filename)
            throws IOException, DatabaseErrorException, UnknownInputException, EntityErrorException;
}
