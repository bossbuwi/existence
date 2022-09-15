package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.AuthWrapper;

import java.util.List;

public interface UserAccessService {
    Long countUsers();
    List<UserDTO> getAllUsers();
    UserDTO getUser(long id)
            throws EntityErrorException, UnknownInputException;
    UserDTO getDetailedUser(long id) throws EntityErrorException;
    UserDTO createUser(AuthWrapper user)
            throws AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
    UserDTO updateUserPassword(AuthWrapper user)
            throws UnknownInputException, AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
    UserDTO deleteUser(AuthWrapper user)
            throws UnknownInputException, AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
    UserDTO addRoles(AuthWrapper user)
            throws AuthorizationErrorException, UnknownInputException, EntityErrorException, DatabaseErrorException;
    UserDTO removeRoles(AuthWrapper user)
            throws AuthorizationErrorException, UnknownInputException, EntityErrorException, DatabaseErrorException;
    UserDTO banUser(AuthWrapper user)
            throws UnknownInputException, AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
    UserDTO unbanUser(AuthWrapper user)
            throws UnknownInputException, AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
}
