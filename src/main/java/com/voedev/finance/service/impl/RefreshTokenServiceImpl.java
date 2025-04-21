package com.voedev.finance.service.impl;

import com.voedev.finance.exception.TokenException;
import com.voedev.finance.model.dto.auth.request.RefreshTokenRequest;
import com.voedev.finance.model.dto.auth.response.RefreshTokenResponse;
import com.voedev.finance.model.entity.RefreshToken;
import com.voedev.finance.model.entity.User;
import com.voedev.finance.model.enums.user.TokenType;
import com.voedev.finance.repository.RefreshTokenRepository;
import com.voedev.finance.repository.UserRepository;
import com.voedev.finance.service.JwtService;
import com.voedev.finance.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Value("${application.security.jwt.refresh-token.cookie-name}")
    private String refreshTokenName;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .revoked(false)
                .user(user)
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenException(token.getToken(), "Refresh token was expired. Please make a new authentication request");
            // Please see the refresh_token update guidelines
        }
        return token;
    }

    @Override
    @Transactional
    public RefreshTokenResponse generateNewToken(RefreshTokenRequest request) {
        User refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new TokenException(request.getRefreshToken(), "Refresh token does not exist."));

        String token = jwtService.generateToken(refreshToken);
        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(request.getRefreshToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseCookie generateRefreshTokenCookie(String token) {
        refreshTokenRepository.findByToken(token)
                .map(this::verifyExpiration)
                .orElseThrow(() -> new TokenException(token, "Refresh token does not exist."));

        return ResponseCookie.from(refreshTokenName, token)
                .path("/")
                .maxAge(refreshTokenExpiration / 1000) // 15 days in seconds
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenName);
        return cookie != null ? cookie.getValue() : null;
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenException(token, "Refresh token does not exist."));
        refreshTokenRepository.delete(refreshToken);
    }

    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }
}
