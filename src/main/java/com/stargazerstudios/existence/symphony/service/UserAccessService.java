package com.stargazerstudios.existence.symphony.service;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;

import java.util.List;

public interface UserAccessService {
    List<UserDTO> getAllUsers();
    UserDTO getUser(UserWrapper user) throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException;
    UserDTO createUser(UserWrapper user)
            throws DuplicateEntityException, InvalidInputException, UserUnauthorizedException, DatabaseErrorException, EntityNotFoundException;
    UserDTO updateUserPassword(UserWrapper user)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException, DatabaseErrorException;
    UserDTO deleteUser(UserWrapper user)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException;
    UserDTO addRoles(UserWrapper user)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException;
    UserDTO removeRoles(UserWrapper user)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException;
    UserDTO banUser(UserWrapper user)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException;
    UserDTO unbanUser(UserWrapper user)
            throws EntityNotFoundException, InvalidInputException, UserUnauthorizedException;
}
