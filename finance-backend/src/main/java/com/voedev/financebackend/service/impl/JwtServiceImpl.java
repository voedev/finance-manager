package com.voedev.financebackend.service.impl;

import com.voedev.financebackend.service.JwtService;
import com.voedev.financebackend.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtUtils jwtUtils;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.cookie-name}")
    private String jwtCookieName;

    @Override
    public String extractUsername(String token) {
        return jwtUtils.extractClaim(token, Claims::getSubject, secretKey);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        var username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !jwtUtils.isTokenExpired(token, secretKey));
    }

    @Override
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        return cookie != null ? cookie.getValue() : null;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public ResponseCookie generateJwtCookie(String jwt) {
        return ResponseCookie.from(jwtCookieName, jwt)
                .path("/")
                .maxAge(24 * 60 * 60) // 24 hours
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    @Override
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return jwtUtils.buildToken(extraClaims, userDetails.getUsername(), jwtExpiration, secretKey);
    }
}