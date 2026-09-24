package com.auth_engine.service;

import com.auth_engine.dto.UserRequestDto;
import com.auth_engine.dto.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthenticationService {

    public UserResponseDto createUser(UserRequestDto user);
    public List<UserResponseDto> getAllUsers();

}
