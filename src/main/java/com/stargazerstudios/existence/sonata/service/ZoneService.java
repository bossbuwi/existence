package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.entity.Zone;
import com.stargazerstudios.existence.sonata.wrapper.ZoneWrapper;

import java.util.List;

public interface ZoneService {
    List<ZoneDTO> getAllZones();
    ZoneDTO createZone(ZoneWrapper wZone)
            throws AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
    ZoneDTO updateZone(ZoneWrapper wZone)
            throws AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
    ZoneDTO deleteZone(long id) throws EntityErrorException;
}
