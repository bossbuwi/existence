package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.AuthWrapper;

import java.util.List;

public interface UserAccessService {
    Long countUsers();
    List<UserDTO> getAllUsers();
    UserDTO getUser(long id)
            throws EntityException;
    UserDTO getDetailedUser(long id) throws EntityException;
    UserDTO createUser(AuthWrapper user)
            throws AuthorizationException, EntityException, DatabaseException;
    UserDTO updateUserPassword(AuthWrapper user)
            throws UnknownInputException, AuthorizationException, EntityException, DatabaseException;
    UserDTO deleteUser(AuthWrapper user)
            throws AuthorizationException, EntityException, DatabaseException;
    UserDTO modifyRoles(AuthWrapper user)
            throws AuthorizationException, UnknownInputException, EntityException, DatabaseException;
    UserDTO addRoles(AuthWrapper user)
            throws AuthorizationException, UnknownInputException, EntityException, DatabaseException;
    UserDTO removeRoles(AuthWrapper user)
            throws AuthorizationException, UnknownInputException, EntityException, DatabaseException;
    UserDTO banUser(AuthWrapper user)
            throws UnknownInputException, AuthorizationException, EntityException, DatabaseException;
    UserDTO unbanUser(AuthWrapper user)
            throws UnknownInputException, AuthorizationException, EntityException, DatabaseException;
}
