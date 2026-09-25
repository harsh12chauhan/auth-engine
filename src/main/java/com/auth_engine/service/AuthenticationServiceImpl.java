package com.auth_engine.service;

import com.auth_engine.dto.UserRequestDto;
import com.auth_engine.dto.UserResponseDto;
import com.auth_engine.entity.User;
import com.auth_engine.repository.UserRepository;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto user) {

        User newUser = new User();

        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setHashedPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEnabled(true);

        User savedUser = userRepository.save(newUser);

        return UserResponseDtoMapper(savedUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserResponseDto> userDtoList = new ArrayList<>();

        for(var user:users){
            userDtoList.add(UserResponseDtoMapper(user));
        }

        return userDtoList;
    }


    // Utils
    private static @NonNull UserResponseDto UserResponseDtoMapper(User savedUser) {

        UserResponseDto dto = new UserResponseDto();

        dto.setId(savedUser.getId());
        dto.setEmail(savedUser.getEmail());
        dto.setUsername(savedUser.getUsername());
        dto.setCreatedAt(savedUser.getCreatedAt());
        dto.setEnabled(savedUser.isEnabled());

        return dto;
    }

}
