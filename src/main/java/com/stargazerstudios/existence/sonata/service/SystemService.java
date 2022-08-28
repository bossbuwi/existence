package com.stargazerstudios.existence.sonata.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.sonata.dto.SystemDTO;
import com.stargazerstudios.existence.sonata.wrapper.SystemWrapper;

import java.util.List;

public interface SystemService {
    List<SystemDTO> getSystems();
    List<SystemDTO> getFullSystems();
    Long countSystems();
    SystemDTO createSystem(SystemWrapper wSystem)
            throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException;
    SystemDTO updateSystem(SystemWrapper wSystem)
            throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException;
    SystemDTO deleteSystem(long id)
            throws EntityErrorException, DatabaseErrorException, AuthorizationErrorException;
    SystemDTO createFullSystem(SystemWrapper wSystem)
            throws AuthorizationErrorException, DatabaseErrorException, EntityErrorException, UnknownInputException;
}
