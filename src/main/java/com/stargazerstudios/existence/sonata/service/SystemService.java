package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;

import java.util.List;

public interface SystemService {
    List<SystemDTO> getAllSystems();
    Long countSystems();
    SystemDTO createSystem(SystemWrapper wSystem)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException;
}
