package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;

import java.util.List;

public interface SystemService {
    List<SystemDTO> getSystems();
    List<SystemDTO> getFullSystems();
    List<SystemDTO> getSystemsOnMachine(String machine);
    Long countSystems();
    SystemDTO createSystem(SystemWrapper wSystem)
            throws EntityException, DatabaseException, AuthorizationException;
    SystemDTO updateSystem(SystemWrapper wSystem)
            throws EntityException, DatabaseException, AuthorizationException;
    SystemDTO deleteSystem(long id)
            throws EntityException, DatabaseException, AuthorizationException;
    SystemDTO createFullSystem(SystemWrapper wSystem)
            throws AuthorizationException, DatabaseException, EntityException, UnknownInputException;
    SystemDTO updateFullSystem(SystemWrapper wSystem)
            throws AuthorizationException, DatabaseException, EntityException, UnknownInputException;
}
