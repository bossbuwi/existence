package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.sonata.dto.ZoneDTO;
import com.stargazerstudios.existence.sonata.wrapper.ZoneWrapper;

import java.util.List;

public interface ZoneService {
    List<ZoneDTO> getAllZones();
    ZoneDTO createZone(ZoneWrapper wZone)
            throws AuthorizationException, EntityException, DatabaseException;
    ZoneDTO updateZone(ZoneWrapper wZone)
            throws AuthorizationException, EntityException, DatabaseException;
    ZoneDTO deleteZone(long id) throws EntityException;
}
