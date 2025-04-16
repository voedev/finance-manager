package com.voedev.finance.service.impl;

import com.voedev.finance.model.dto.user.request.AuthenticationRequest;
import com.voedev.finance.model.dto.user.request.RegisterRequest;
import com.voedev.finance.model.dto.user.response.AuthenticationResponse;
import com.voedev.finance.model.entity.User;
import com.voedev.finance.model.enums.user.TokenType;
import com.voedev.finance.repository.UserRepository;
import com.voedev.finance.service.AuthenticationService;
import com.voedev.finance.service.JwtService;
import com.voedev.finance.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthenticationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .role(user.getRole().name())
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return null;
    }
}
