package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.conductor.erratum.root.*;
import com.stargazerstudios.existence.conductor.erratum.root.DatabaseErrorException;
import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.service.AuthenticationServiceImpl;
import com.stargazerstudios.existence.symphony.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/concerto/")
public class AuthenticationController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserWrapper user)
            throws AuthorizationErrorException, SystemErrorException,
                DatabaseErrorException, UnknownInputException,
                ThirdPartyErrorException, EntityErrorException {
        return new ResponseEntity<>(authenticationService.login(user), HttpStatus.OK);
    }

    @PostMapping("/autologin")
    public ResponseEntity<UserDTO> autoLogin(String token) throws AuthorizationErrorException {
        return new ResponseEntity<>(authenticationService.autologin(token), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(authenticationService.logout(request, response), HttpStatus.OK);
    }
}
