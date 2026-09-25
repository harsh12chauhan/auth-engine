package com.auth_engine.service;

import com.auth_engine.dto.UserLoginRequestDto;
import com.auth_engine.dto.UserRequestDto;
import com.auth_engine.dto.UserResponseDto;
import com.auth_engine.entity.User;
import com.auth_engine.repository.UserRepository;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        Optional<User> userOptional = userRepository.findByEmail(userRequestDto.getEmail());
        if(userOptional.isPresent()){
            throw new RuntimeException("User with email already exist's.");
        }

        userOptional= userRepository.findByUsername(userRequestDto.getUsername());
        if(userOptional.isPresent()){
            throw new RuntimeException("User with username already exist's.");
        }

        User newUser = new User();

        newUser.setUsername(userRequestDto.getUsername());
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setHashedPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        newUser.setEnabled(true);

        User savedUser = userRepository.save(newUser);

        return UserResponseDtoMapper(savedUser);
    }

    @Override
    public UserResponseDto authenticateUser(UserLoginRequestDto userLoginRequestDto){

        User user = userRepository.findByEmail(userLoginRequestDto.getEmail()).orElseThrow(()-> new RuntimeException("Invalid user credentials"));

        boolean isPasswordValid = passwordEncoder.matches(userLoginRequestDto.getPassword(),user.getHashedPassword());

        if(!isPasswordValid){
            throw new RuntimeException("Invalid user credentials");
        }

        return UserResponseDtoMapper(user);
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
