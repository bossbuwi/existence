package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.universal.DuplicateEntityException;
import com.stargazerstudios.existence.conductor.erratum.universal.EntityNotFoundException;
import com.stargazerstudios.existence.conductor.erratum.universal.InvalidInputException;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.wrapper.ZoneWrapper;

import java.util.List;

public interface ZoneService {
    List<ZoneDTO> getAllZones();
    ZoneDTO createZone(ZoneWrapper wZone)
            throws EntityNotFoundException, DuplicateEntityException, InvalidInputException;
    ZoneDTO updateZone(ZoneWrapper wZone)
            throws EntityNotFoundException, DuplicateEntityException, InvalidInputException;
}
