package com.voedev.finance.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String getJwtFromCookies(HttpServletRequest request);

    String generateToken(UserDetails userDetails);

    ResponseCookie generateJwtCookie(String jwt);
}
