package com.auth_engine.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private boolean isEnabled;
    private LocalDateTime createdAt;

}
