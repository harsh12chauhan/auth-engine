package com.auth_engine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponseDto {

    private String accessToken;
    private String tokenType;
}
