package com.auth_engine.controller;

import com.auth_engine.dto.UserLoginRequestDto;
import com.auth_engine.dto.UserRequestDto;
import com.auth_engine.dto.UserResponseDto;
import com.auth_engine.service.AuthenticationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto user){
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.createUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> authenticate(@Valid @RequestBody UserLoginRequestDto user){
        return ResponseEntity.status(HttpStatus.FOUND).body(authenticationService.authenticateUser(user));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUser(){
        return ResponseEntity.ok(authenticationService.getAllUsers());
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Testing");
    }

}
