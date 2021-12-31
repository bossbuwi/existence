package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;

import java.util.List;

public interface SystemService {
    List<SystemDTO> getAllSystems();
    SystemDTO createSystem(SystemWrapper wSystem) throws DuplicateEntityException, EntityNotFoundException, InvalidInputException;

}
