package com.auth_engine.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApiErrorResponseDto {

    public LocalDateTime timeStamp;
    public HttpStatusCode status;
    public String error;
    public String message;
    public String path;
}
