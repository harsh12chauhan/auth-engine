package com.auth_engine.service;

import com.auth_engine.dto.TokenResponseDto;
import com.auth_engine.dto.UserLoginRequestDto;
import com.auth_engine.dto.UserRequestDto;
import com.auth_engine.dto.UserResponseDto;
import com.auth_engine.entity.User;
import com.auth_engine.enums.Role;
import com.auth_engine.exception.EmailAlreadyExistsException;
import com.auth_engine.exception.InvalidCredentialsException;
import com.auth_engine.exception.UsernameAlreadyExistsException;
import com.auth_engine.repository.UserRepository;

import com.auth_engine.security.token.JwtService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws EmailAlreadyExistsException, UsernameAlreadyExistsException {

        Optional<User> userOptional = userRepository.findByEmail(userRequestDto.getEmail());
        if(userOptional.isPresent()){
            throw new EmailAlreadyExistsException("User with email already exists.");
        }

        userOptional= userRepository.findByUsername(userRequestDto.getUsername());
        if(userOptional.isPresent()){
            throw new UsernameAlreadyExistsException("User with username already exists.");
        }

        User newUser = new User();

        newUser.setUsername(userRequestDto.getUsername());
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setHashedPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        newUser.setEnabled(true);
        newUser.setRole(Role.USER);

        User savedUser = userRepository.save(newUser);

        return UserResponseDtoMapper(savedUser);
    }

    @Override
    public TokenResponseDto authenticateUser(UserLoginRequestDto userLoginRequestDto) throws InvalidCredentialsException{

        User user = userRepository.findByEmail(userLoginRequestDto.getEmail())
                .orElseThrow(()-> new InvalidCredentialsException("Invalid user credentials"));

        boolean isPasswordValid = passwordEncoder.matches(userLoginRequestDto.getPassword(),user.getHashedPassword());

        if(!isPasswordValid){
            throw new InvalidCredentialsException("Invalid user credentials");
        }

        String token = jwtService.generateToken(
                user.getId().toString(),
                user.getRole().name()
        );

        TokenResponseDto tokenDto = new TokenResponseDto();
        tokenDto.setAccessToken(token);
        tokenDto.setTokenType("Bearer");

        return tokenDto;
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
