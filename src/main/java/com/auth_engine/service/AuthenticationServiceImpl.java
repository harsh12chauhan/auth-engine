package com.auth_engine.service;

import com.auth_engine.dto.UserRequestDto;
import com.auth_engine.dto.UserResponseDto;
import com.auth_engine.entity.User;
import com.auth_engine.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        userRepository.save(newUser);

        UserResponseDto dto = new UserResponseDto();
        dto.setId(newUser.getId());
        dto.setEmail(newUser.getEmail());
        dto.setUsername(newUser.getUsername());
        dto.setCreatedAt(newUser.getCreatedAt());
        dto.setEnabled(newUser.isEnabled());

        return dto;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        List<User> users = userRepository.findAll();

        List<UserResponseDto> userDtoList = new ArrayList<>();

        for(var user:users){

            UserResponseDto tempDto = new UserResponseDto();
            tempDto.setId(user.getId());
            tempDto.setEmail(user.getEmail());
            tempDto.setUsername(user.getUsername());
            tempDto.setCreatedAt(user.getCreatedAt());
            tempDto.setEnabled(user.isEnabled());

            userDtoList.add(tempDto);
        }

        return userDtoList;
    }
}
