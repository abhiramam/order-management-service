package org.scaler.ordermanagementservice.controllers;

import jakarta.validation.Valid;
import org.scaler.ordermanagementservice.dtos.CreateUserRequestDto;
import org.scaler.ordermanagementservice.dtos.UserResponseDto;
import org.scaler.ordermanagementservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }
}
