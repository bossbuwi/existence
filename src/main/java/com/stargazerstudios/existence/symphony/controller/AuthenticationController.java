package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.service.AuthenticationServiceImpl;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/concerto/")
public class AuthenticationController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserWrapper user)
            throws UserNotFoundException, BadGatewayException, EntityNotFoundException,
                InvalidInputException, FatalErrorException, InvalidPropertyErrorException {
        return new ResponseEntity<>(authenticationService.login(user), HttpStatus.OK);
    }

    @PostMapping("/autologin")
    public ResponseEntity<UserDTO> autoLogin(String token) throws UserNotFoundException {
        return new ResponseEntity<>(authenticationService.autologin(token), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
