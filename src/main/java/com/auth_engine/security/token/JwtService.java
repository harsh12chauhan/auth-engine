package com.auth_engine.security.token;

import org.springframework.stereotype.Service;

public interface JwtService {

    public String generateToken(String subject, String role);
}
