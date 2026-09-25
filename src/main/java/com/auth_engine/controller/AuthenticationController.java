package com.auth_engine.controller;

import com.auth_engine.dto.TokenResponseDto;
import com.auth_engine.dto.UserLoginRequestDto;
import com.auth_engine.dto.UserRequestDto;
import com.auth_engine.dto.UserResponseDto;
import com.auth_engine.service.AuthenticationService;

import jakarta.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/auth")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto user){
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.createUser(user));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> authenticate(@Valid @RequestBody UserLoginRequestDto user){
        return ResponseEntity.ok(authenticationService.authenticateUser(user));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUser(){
        return ResponseEntity.ok(authenticationService.getAllUsers());
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Testing");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminTest() {
        return ResponseEntity.ok("Admin access granted");
    }
}
