package com.voedev.finance.service;

import com.voedev.finance.model.dto.user.request.RefreshTokenRequest;
import com.voedev.finance.model.dto.user.response.RefreshTokenResponse;
import com.voedev.finance.model.entity.RefreshToken;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);

    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);

    ResponseCookie generateRefreshTokenCookie(String token);
}
