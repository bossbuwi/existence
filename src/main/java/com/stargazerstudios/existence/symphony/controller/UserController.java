package com.stargazerstudios.existence.symphony.controller;

import com.stargazerstudios.existence.symphony.dto.UserDTO;
import com.stargazerstudios.existence.symphony.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/symphony/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/index")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
}
