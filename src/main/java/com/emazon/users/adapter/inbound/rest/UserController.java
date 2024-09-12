package com.emazon.users.adapter.inbound.rest;

import com.emazon.users.application.dto.UserDTO;
import com.emazon.users.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint para crear un usuario auxiliar de bodega
    @PostMapping("/create-aux-bodega")
    public ResponseEntity<UserDTO> createAuxBodegaUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            UserDTO createdUser = userService.createAuxBodegaUser(userDTO);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
