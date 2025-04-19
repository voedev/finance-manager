package com.voedev.finance.service;

import com.voedev.finance.exception.EmailAlreadyExistsException;
import com.voedev.finance.model.dto.auth.request.RegisterRequest;
import com.voedev.finance.model.dto.auth.response.AuthenticationResponse;
import com.voedev.finance.model.entity.RefreshToken;
import com.voedev.finance.model.entity.User;
import com.voedev.finance.model.enums.user.TokenType;
import com.voedev.finance.model.enums.user.UserRole;
import com.voedev.finance.model.enums.user.UserStatus;
import com.voedev.finance.repository.UserRepository;
import com.voedev.finance.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private RegisterRequest registerRequest;

    private User savedUser;

    private RefreshToken refreshToken;

    private String accessToken;

    @BeforeEach
    void init() {
        registerRequest = RegisterRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();

        savedUser = User.builder()
                .id(1L)
                .email(registerRequest.getEmail())
                .password("encodedPassword")
                .role(UserRole.USER)
                .status(UserStatus.VERIFY_EMAIL)
                .build();

        refreshToken = RefreshToken.builder()
                .id(1L)
                .token("1LGeneratedRefreshToken")
                .build();

        accessToken = "1LGeneratedToken";
    }

    @Test
    void register_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authenticationService.register(registerRequest))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void register_WhenSuccessSave() {
        // Given
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(savedUser.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn(accessToken);
        when(refreshTokenService.createRefreshToken(savedUser.getId())).thenReturn(refreshToken);

        // When
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);

        // Then
        assertThat(authenticationResponse).isNotNull()
                .satisfies(response -> {
                    assertThat(response.getId()).isEqualTo(1L);
                    assertThat(response.getEmail()).isEqualTo(savedUser.getEmail());
                    assertThat(response.getAccessToken()).isEqualTo(accessToken);
                    assertThat(response.getRefreshToken()).isEqualTo(refreshToken.getToken());
                    assertThat(response.getRefreshToken()).isEqualTo(refreshToken.getToken());
                    assertThat(authenticationResponse.getRoles()).contains("ROLE_USER");
                    assertThat(authenticationResponse.getTokenType()).isEqualTo(TokenType.BEARER.name());
                });

        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(UserDetails.class));
        verify(refreshTokenService).createRefreshToken(savedUser.getId());
    }


}



















