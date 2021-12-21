package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.universal.*;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.service.AuthenticationServiceImpl;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/concerto/")
public class AuthenticationController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserWrapper user)
            throws UserNotFoundException, BadGatewayException, GatewayTimeoutException, EntityNotFoundException {
        return new ResponseEntity<>(authenticationService.login(user), HttpStatus.OK);
    }

    // TODO Not yet done
    @PostMapping("/autologin")
    public ResponseEntity<UserDTO> autoLogin() throws UserNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return null;
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
