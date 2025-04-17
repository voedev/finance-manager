package com.voedev.finance.service;

import com.voedev.finance.model.dto.auth.request.RefreshTokenRequest;
import com.voedev.finance.model.dto.auth.response.RefreshTokenResponse;
import com.voedev.finance.model.entity.RefreshToken;
import org.springframework.http.ResponseCookie;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);

    ResponseCookie generateRefreshTokenCookie(String token);
}
