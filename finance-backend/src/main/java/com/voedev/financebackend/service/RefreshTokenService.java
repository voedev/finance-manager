package com.voedev.financebackend.service;

import com.voedev.financebackend.model.dto.auth.request.RefreshTokenRequest;
import com.voedev.financebackend.model.dto.auth.response.RefreshTokenResponse;
import com.voedev.financebackend.model.entity.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);

    RefreshToken verifyExpiration(RefreshToken token);

    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);

    ResponseCookie generateRefreshTokenCookie(String token);

    String getRefreshTokenFromCookies(HttpServletRequest request);

    void deleteByToken(String token);

    ResponseCookie getCleanRefreshTokenCookie();

}
