package com.voedev.finance.service.impl;

import com.voedev.finance.model.dto.user.request.RefreshTokenRequest;
import com.voedev.finance.model.dto.user.response.RefreshTokenResponse;
import com.voedev.finance.model.entity.RefreshToken;
import com.voedev.finance.model.entity.User;
import com.voedev.finance.repository.RefreshTokenRepository;
import com.voedev.finance.repository.UserRepository;
import com.voedev.finance.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Value("${application.security.jwt.refresh-token.cookie-name}")
    private String refreshTokenName;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .revoked(false)
                .user(user)
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        return null;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return Optional.empty();
    }

    @Override
    public RefreshTokenResponse generateNewToken(RefreshTokenRequest request) {
        return null;
    }
}
