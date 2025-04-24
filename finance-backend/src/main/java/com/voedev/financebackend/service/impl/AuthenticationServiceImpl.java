package com.voedev.financebackend.service.impl;

import com.voedev.financebackend.exception.EmailAlreadyExistsException;
import com.voedev.financebackend.model.dto.auth.request.AuthenticationRequest;
import com.voedev.financebackend.model.dto.auth.request.RegisterRequest;
import com.voedev.financebackend.model.dto.auth.response.AuthenticationResponse;
import com.voedev.financebackend.model.event.WelcomeEmailEvent;
import com.voedev.financebackend.model.entity.User;
import com.voedev.financebackend.model.enums.user.TokenType;
import com.voedev.financebackend.publisher.EventPublisher;
import com.voedev.financebackend.repository.UserRepository;
import com.voedev.financebackend.service.AuthenticationService;
import com.voedev.financebackend.service.JwtService;
import com.voedev.financebackend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private final AuthenticationManager authenticationManager;
    private final EventPublisher<WelcomeEmailEvent> eventPublisher;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail(), "The email address is already registered.");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        user = userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        eventPublisher.publish(new WelcomeEmailEvent(user.getEmail(), user.getCreatedAt()));

        return AuthenticationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .roles(roles)
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthenticationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .roles(roles)
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }
}
