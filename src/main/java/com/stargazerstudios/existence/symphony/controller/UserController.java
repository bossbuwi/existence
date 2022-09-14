package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.root.AuthorizationErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.EntityErrorException;
import com.stargazerstudios.existence.conductor.erratum.root.UnknownInputException;
import com.stargazerstudios.existence.conductor.validation.groups.*;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.service.UserAccessService;
import com.stargazerstudios.existence.symphony.wrapper.AuthWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/symphony")
public class UserController {

    @Autowired
    private UserAccessService userAccessService;

    /* Unguarded Enpoints */
    @GetMapping("/con/users/count")
    public ResponseEntity<Long> countUsers() {
        return new ResponseEntity<>(userAccessService.countUsers(), HttpStatus.OK);
    }

    /* Guarded Endpoints */
    @GetMapping("/users/index")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userAccessService.getAllUsers(), HttpStatus.OK);
    }

    // TODO: Rework this. GET request should use params and not an HTTP body.
    @GetMapping("/users/user")
    public ResponseEntity<UserDTO> getUser(@Validated(GetValidation.class)
                                               @RequestBody AuthWrapper user)
            throws EntityErrorException {
        return new ResponseEntity<>(userAccessService.getUser(user), HttpStatus.OK);
    }

    @GetMapping("/users/user/{id}/details")
    public ResponseEntity<UserDTO> getDetailedUser(@NotBlank @PathVariable("id") long id)
            throws EntityErrorException {
        return new ResponseEntity<>(userAccessService.getDetailedUser(id), HttpStatus.OK);
    }

    @PostMapping("/users/user")
    public ResponseEntity<UserDTO> createUser(@Validated(PostValidation.class)
                                                  @RequestBody AuthWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.createUser(user), HttpStatus.OK);
    }

    @PutMapping("/users/user/change-password")
    public ResponseEntity<UserDTO> updateUserPassword(@Validated(PutValidation.class)
                                                          @RequestBody AuthWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.updateUserPassword(user), HttpStatus.OK);
    }

    @PutMapping("/users/user/add-roles")
    public ResponseEntity<UserDTO> addRoles(@Validated(PutRelationValidation.class)
                                                @RequestBody AuthWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.addRoles(user), HttpStatus.OK);
    }

    @PutMapping("/users/user/remove-roles")
    public ResponseEntity<UserDTO> removeRoles(@Validated(PutRelationValidation.class)
                                                   @RequestBody AuthWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.removeRoles(user), HttpStatus.OK);
    }

    @PutMapping("/users/user/ban-user")
    public ResponseEntity<UserDTO> banUser(@Validated(AuthValidation.class)
                                               @RequestBody AuthWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.banUser(user), HttpStatus.OK);
    }

    @PutMapping("/users/user/unban-user")
    public ResponseEntity<UserDTO> unbanUser(@Validated(AuthValidation.class)
                                                 @RequestBody AuthWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.unbanUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/users/user")
    public ResponseEntity<UserDTO> deleteUser(@Validated(DeleteValidation.class)
                                                  @RequestBody AuthWrapper user)
            throws AuthorizationErrorException, DatabaseErrorException,
                UnknownInputException, EntityErrorException {
        return new ResponseEntity<>(userAccessService.deleteUser(user), HttpStatus.OK);
    }
}
