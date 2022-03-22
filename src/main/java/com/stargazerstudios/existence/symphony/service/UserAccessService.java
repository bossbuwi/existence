package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;

import java.util.List;

public interface UserAccessService {
    List<UserDTO> getAllUsers();
    UserDTO getUser(UserWrapper user) throws UnknownInputException, EntityErrorException;
    UserDTO createUser(UserWrapper user) throws AuthorizationErrorException, UnknownInputException, EntityErrorException, DatabaseErrorException;
    UserDTO updateUserPassword(UserWrapper user) throws UnknownInputException, AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
    UserDTO deleteUser(UserWrapper user) throws UnknownInputException, AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
    UserDTO addRoles(UserWrapper user) throws AuthorizationErrorException, UnknownInputException, EntityErrorException, DatabaseErrorException;
    UserDTO removeRoles(UserWrapper user) throws AuthorizationErrorException, UnknownInputException, EntityErrorException, DatabaseErrorException;
    UserDTO banUser(UserWrapper user) throws UnknownInputException, AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
    UserDTO unbanUser(UserWrapper user) throws UnknownInputException, AuthorizationErrorException, EntityErrorException, DatabaseErrorException;
}
