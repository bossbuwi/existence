package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.wrapper.ZoneWrapper;

import java.util.List;

public interface ZoneService {
    List<ZoneDTO> getAllZones();
    ZoneDTO createZone(ZoneWrapper wZone)
            throws UnknownInputException, EntityErrorException, DatabaseErrorException;
    ZoneDTO updateZone(ZoneWrapper wZone);
}
