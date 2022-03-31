package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.validation.groups.*;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.service.UserAccessService;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/symphony/users")
public class UserController {

    @Autowired
    private UserAccessService userAccessService;

    @GetMapping("/index")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userAccessService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUser(@Validated(GetValidation.class)
                                               @RequestBody UserWrapper user)
            throws UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.getUser(user), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<UserDTO> createUser(@Validated(PostValidation.class)
                                                  @RequestBody UserWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.createUser(user), HttpStatus.OK);
    }

    @PutMapping("/user/change-password")
    public ResponseEntity<UserDTO> updateUserPassword(@Validated(PutValidation.class)
                                                          @RequestBody UserWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.updateUserPassword(user), HttpStatus.OK);
    }

    @PutMapping("/user/add-roles")
    public ResponseEntity<UserDTO> addRoles(@Validated(PutRelationValidation.class)
                                                @RequestBody UserWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.addRoles(user), HttpStatus.OK);
    }

    @PutMapping("/user/remove-roles")
    public ResponseEntity<UserDTO> removeRoles(@Validated(PutRelationValidation.class)
                                                   @RequestBody UserWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.removeRoles(user), HttpStatus.OK);
    }

    @PutMapping("/user/ban-user")
    public ResponseEntity<UserDTO> banUser(@Validated(AuthValidation.class)
                                               @RequestBody UserWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.banUser(user), HttpStatus.OK);
    }

    @PutMapping("/user/unban-user")
    public ResponseEntity<UserDTO> unbanUser(@Validated(AuthValidation.class)
                                                 @RequestBody UserWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.unbanUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/user")
    public ResponseEntity<UserDTO> deleteUser(@Validated(DeleteValidation.class)
                                                  @RequestBody UserWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.deleteUser(user), HttpStatus.OK);
    }
}
