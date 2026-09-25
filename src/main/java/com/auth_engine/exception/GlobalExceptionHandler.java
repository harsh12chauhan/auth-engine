package com.auth_engine.exception;

import com.auth_engine.dto.ApiErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, HttpServletRequest request){

        ApiErrorResponseDto dto = new ApiErrorResponseDto();

        dto.setError("Conflict");
        dto.setTimeStamp(LocalDateTime.now());
        dto.setMessage("User with email already exist's.");
        dto.setStatus(HttpStatus.CONFLICT);
        dto.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex, HttpServletRequest request){

        ApiErrorResponseDto dto = new ApiErrorResponseDto();

        dto.setError("Conflict");
        dto.setTimeStamp(LocalDateTime.now());
        dto.setMessage("User with username already exist's.");
        dto.setStatus(HttpStatus.CONFLICT);
        dto.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponseDto> handleInvalidCredentialsException(InvalidCredentialsException ex, @NonNull HttpServletRequest request){

        ApiErrorResponseDto dto = new ApiErrorResponseDto();

        dto.setError("Bad_Request");
        dto.setTimeStamp(LocalDateTime.now());
        dto.setMessage("Invalid user credentials");
        dto.setStatus(HttpStatus.BAD_REQUEST);
        dto.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDto> handleOtherException(Exception ex,HttpServletRequest request){
        ApiErrorResponseDto dto = new ApiErrorResponseDto();

        dto.setError("Internal_Server_Error");
        dto.setTimeStamp(LocalDateTime.now());
        dto.setMessage("Internal server error");
        dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        dto.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }

}
