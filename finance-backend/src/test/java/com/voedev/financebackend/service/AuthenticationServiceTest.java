package com.voedev.financebackend.service;

import com.voedev.financebackend.exception.EmailAlreadyExistsException;
import com.voedev.financebackend.model.dto.auth.request.AuthenticationRequest;
import com.voedev.financebackend.model.dto.auth.request.RegisterRequest;
import com.voedev.financebackend.model.dto.auth.response.AuthenticationResponse;
import com.voedev.financebackend.model.dto.event.WelcomeEmailEvent;
import com.voedev.financebackend.model.entity.RefreshToken;
import com.voedev.financebackend.model.entity.User;
import com.voedev.financebackend.model.enums.user.TokenType;
import com.voedev.financebackend.model.enums.user.UserRole;
import com.voedev.financebackend.model.enums.user.UserStatus;
import com.voedev.financebackend.publisher.EventPublisher;
import com.voedev.financebackend.repository.UserRepository;
import com.voedev.financebackend.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private EventPublisher<WelcomeEmailEvent> eventPublisher;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private static final String TEST_USER_EMAIL = "email@email.com";
    private static final String TEST_REFRESH_TOKEN = "1LGeneratedRefreshToken";
    private static final String TEST_ACCESS_TOKEN = "1LGeneratedToken";

    private RefreshToken refreshToken;
    private User userEntity;

    @BeforeEach
    void baseSetUp() {
        userEntity = User.builder()
                .id(1L)
                .email(TEST_USER_EMAIL)
                .password("encodedPassword")
                .role(UserRole.USER)
                .status(UserStatus.VERIFY_EMAIL)
                .build();

        refreshToken = RefreshToken.builder()
                .id(1L)
                .token(TEST_REFRESH_TOKEN)
                .build();
    }

    @Nested
    @DisplayName("AuthenticationService.register() tests")
    class RegisterTests {

        private RegisterRequest registerRequest;

        @BeforeEach
        void registerSetUp() {
            registerRequest = RegisterRequest.builder()
                    .email(TEST_USER_EMAIL)
                    .password("password")
                    .build();
        }

        @Test
        void register_WhenEmailAlreadyExists() {
            when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

            assertThatThrownBy(() -> authenticationService.register(registerRequest))
                    .isInstanceOf(EmailAlreadyExistsException.class);
        }

        @Test
        void register_WhenSuccessSave() {
            when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
            when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(userEntity.getPassword());
            when(userRepository.save(any(User.class))).thenReturn(userEntity);
            when(jwtService.generateToken(any(UserDetails.class))).thenReturn(TEST_ACCESS_TOKEN);
            when(refreshTokenService.createRefreshToken(userEntity.getId())).thenReturn(refreshToken);

            AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);

            assertThat(authenticationResponse).isNotNull()
                    .satisfies(response -> {
                        assertThat(response.getId()).isEqualTo(1L);
                        assertThat(response.getEmail()).isEqualTo(userEntity.getEmail());
                        assertThat(response.getAccessToken()).isEqualTo(TEST_ACCESS_TOKEN);
                        assertThat(response.getRefreshToken()).isEqualTo(refreshToken.getToken());
                        assertThat(response.getRefreshToken()).isEqualTo(refreshToken.getToken());
                        assertThat(authenticationResponse.getRoles()).contains("ROLE_USER");
                        assertThat(authenticationResponse.getTokenType()).isEqualTo(TokenType.BEARER.name());
                    });

            verify(userRepository).existsByEmail(registerRequest.getEmail());
            verify(passwordEncoder).encode(registerRequest.getPassword());
            verify(userRepository).save(any(User.class));
            verify(jwtService).generateToken(any(UserDetails.class));
            verify(refreshTokenService).createRefreshToken(userEntity.getId());
            verify(eventPublisher).publish(new WelcomeEmailEvent(userEntity.getEmail(), userEntity.getCreatedAt()));
        }
    }

    @Nested
    @DisplayName("AuthenticationService.authenticate() tests")
    class AuthenticateTests {

        private AuthenticationRequest authenticationRequest;

        private Authentication authentication;

        @BeforeEach
        void authenticateSetUp() {
            authenticationRequest = AuthenticationRequest.builder()
                    .email(TEST_USER_EMAIL)
                    .password("password")
                    .build();

            authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword());
        }

        @Test
        void authenticate_WhenUserNotFound_ShouldThrow() {
            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authenticationService.authenticate(authenticationRequest))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(userRepository).findByEmail(authenticationRequest.getEmail());
        }

        @Test
        void authenticate_WhenInvalidCredentials_ShouldThrow() {
            when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

            assertThatThrownBy(() -> authenticationService.authenticate(authenticationRequest))
                    .isInstanceOf(BadCredentialsException.class);

            verify(userRepository, never()).findByEmail(anyString());
        }

        @Test
        void authenticate_WhenSuccess() {
            when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.ofNullable(userEntity));
            when(jwtService.generateToken(any(UserDetails.class))).thenReturn(TEST_ACCESS_TOKEN);
            when(refreshTokenService.createRefreshToken(userEntity.getId())).thenReturn(refreshToken);

            AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

            assertThat(authenticationResponse).isNotNull()
                    .satisfies(response -> {
                        assertThat(response.getId()).isEqualTo(1L);
                        assertThat(response.getEmail()).isEqualTo(userEntity.getEmail());
                        assertThat(response.getAccessToken()).isEqualTo(TEST_ACCESS_TOKEN);
                        assertThat(response.getRefreshToken()).isEqualTo(refreshToken.getToken());
                        assertThat(response.getRefreshToken()).isEqualTo(refreshToken.getToken());
                        assertThat(authenticationResponse.getRoles()).contains("ROLE_USER");
                        assertThat(authenticationResponse.getTokenType()).isEqualTo(TokenType.BEARER.name());
                    });

            verify(userRepository).findByEmail(authenticationRequest.getEmail());
            verify(jwtService).generateToken(any(UserDetails.class));
            verify(refreshTokenService).createRefreshToken(userEntity.getId());
        }
    }

}



















