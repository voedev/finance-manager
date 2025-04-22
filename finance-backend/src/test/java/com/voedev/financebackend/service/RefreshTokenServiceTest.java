package com.voedev.financebackend.service;

import com.voedev.financebackend.exception.TokenException;
import com.voedev.financebackend.model.dto.auth.request.RefreshTokenRequest;
import com.voedev.financebackend.model.dto.auth.response.RefreshTokenResponse;
import com.voedev.financebackend.model.entity.RefreshToken;
import com.voedev.financebackend.model.entity.User;
import com.voedev.financebackend.model.enums.user.TokenType;
import com.voedev.financebackend.model.enums.user.UserRole;
import com.voedev.financebackend.model.enums.user.UserStatus;
import com.voedev.financebackend.repository.RefreshTokenRepository;
import com.voedev.financebackend.repository.UserRepository;
import com.voedev.financebackend.service.impl.RefreshTokenServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Spy
    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    private static final String TEST_USER_EMAIL = "email@email.com";
    private static final String TEST_REFRESH_TOKEN = "1LGeneratedRefreshToken";
    private static final String TEST_ACCESS_TOKEN = "1LGeneratedToken";

    private RefreshToken refreshTokenExpected;
    private RefreshToken refreshTokenExpired;
    private User userEntity;
    private final Integer refreshTokenExpiration = 1296000000;
    private RefreshTokenRequest refreshTokenRequest;

    @BeforeEach
    void setUp() {
        userEntity = User.builder()
                .id(1L)
                .email(TEST_USER_EMAIL)
                .password("encodedPassword")
                .role(UserRole.USER)
                .status(UserStatus.VERIFY_EMAIL)
                .build();

        refreshTokenExpected = RefreshToken.builder()
                .revoked(false)
                .user(userEntity)
                .token(TEST_REFRESH_TOKEN)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();

        refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken(TEST_REFRESH_TOKEN)
                .build();

        refreshTokenExpired = RefreshToken.builder()
                .revoked(false)
                .user(userEntity)
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().minus(1, ChronoUnit.DAYS))
                .build();
    }

    @Test
    void createRefreshToken_WhenUserNotFound_ShouldThrow() {
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.createRefreshToken(userEntity.getId()))
                .isInstanceOf(UsernameNotFoundException.class);

        verify(userRepository).findById(userEntity.getId());
    }

    @Test
    void createRefreshToken_WhenSuccessGenerated() {
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(refreshTokenRepository.save(any())).thenReturn(refreshTokenExpected);

        RefreshToken refreshTokenActual = refreshTokenService.createRefreshToken(userEntity.getId());

        assertThat(refreshTokenActual).isNotNull()
                .satisfies(actual -> {
                    assertThat(actual.getId()).isEqualTo(refreshTokenExpected.getId());
                    assertThat(actual.getUser()).isEqualTo(refreshTokenExpected.getUser());
                    assertThat(actual.getToken()).isEqualTo(refreshTokenExpected.getToken());
                    assertThat(actual.getExpiryDate()).isCloseTo(
                            Instant.now().plusMillis(refreshTokenExpiration), within(3, ChronoUnit.SECONDS));
                    assertThat(actual.isRevoked()).isFalse();
                });

        verify(userRepository).findById(refreshTokenExpected.getUser().getId());
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void generateNewToken_WhenRefreshTokenDoesNotExist_ShouldThrow() {
        when(refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.generateNewToken(refreshTokenRequest))
                .isInstanceOf(TokenException.class);

        verify(refreshTokenRepository).findByToken(refreshTokenRequest.getRefreshToken());
    }

    @Test
    void generateNewToken_WhenVerifyExpired_ShouldThrow() {
        when(refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())).thenReturn(Optional.of(refreshTokenExpired));

        assertThatThrownBy(() -> refreshTokenService.generateNewToken(refreshTokenRequest))
                .isInstanceOf(TokenException.class);

        verify(refreshTokenRepository).findByToken(refreshTokenRequest.getRefreshToken());
        verify(refreshTokenRepository).delete(refreshTokenExpired);
    }

    @Test
    void generateNewToken_WhenSuccessGenerated() {
        when(refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())).thenReturn(Optional.of(refreshTokenExpected));
        when(jwtService.generateToken(any())).thenReturn(TEST_ACCESS_TOKEN);

        RefreshTokenResponse refreshTokenResponse = refreshTokenService.generateNewToken(refreshTokenRequest);

        assertThat(refreshTokenResponse).isNotNull()
                .satisfies(response -> {
                    assertThat(response.getAccessToken()).isEqualTo(TEST_ACCESS_TOKEN);
                    assertThat(response.getRefreshToken()).isEqualTo(refreshTokenExpected.getToken());
                    assertThat(response.getTokenType()).isEqualTo(TokenType.BEARER.name());
                });

        verify(jwtService).generateToken(any());
        verify(refreshTokenRepository).findByToken(refreshTokenRequest.getRefreshToken());
        verify(refreshTokenService).verifyExpiration(refreshTokenExpected);
        verify(refreshTokenService, never()).deleteByToken(refreshTokenRequest.getRefreshToken());
    }

    @Test
    void generateRefreshTokenCookie_WhenTokenNotFound_ShouldThrow() {
        when(refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.generateRefreshTokenCookie(refreshTokenRequest.getRefreshToken()))
                .isInstanceOf(TokenException.class);

        verify(refreshTokenRepository).findByToken(refreshTokenRequest.getRefreshToken());
    }

    @Test
    @DisplayName("When handed over an expired token.")
    void generateRefreshTokenCookie_WhenTokenExpired_ShouldThrow() {
        when(refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())).thenReturn(Optional.of(refreshTokenExpired));

        assertThatThrownBy(() -> refreshTokenService.generateRefreshTokenCookie(refreshTokenRequest.getRefreshToken()))
                .isInstanceOf(TokenException.class);

        verify(refreshTokenRepository).findByToken(refreshTokenRequest.getRefreshToken());
        verify(refreshTokenRepository).delete(refreshTokenExpired);
    }

    @Test
    void generateRefreshTokenCookie_WhenSuccessGenerated_ShouldReturnCookie() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenName", "refresh-jwt-cookie");

        when(refreshTokenRepository.findByToken(TEST_REFRESH_TOKEN)).thenReturn(Optional.of(refreshTokenExpected));
        when(refreshTokenService.verifyExpiration(refreshTokenExpected)).thenReturn(refreshTokenExpected);

        ResponseCookie cookie = refreshTokenService.generateRefreshTokenCookie(TEST_REFRESH_TOKEN);

        assertThat(cookie).isNotNull();
        assertThat(cookie).isInstanceOf(ResponseCookie.class);

        verify(refreshTokenRepository).findByToken(TEST_REFRESH_TOKEN);
    }

    @Test
    void getRefreshTokenFromCookies_WhenCookieExists_ShouldReturnValue() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenName", "refresh-jwt-cookie");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("refresh-jwt-cookie", TEST_REFRESH_TOKEN));

        String token = refreshTokenService.getRefreshTokenFromCookies(request);

        assertThat(token).isEqualTo(TEST_REFRESH_TOKEN);
    }


    @Test
    void getRefreshTokenFromCookies_WhenCookieDoesNotExist_ShouldReturnNull() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenName", "refresh-jwt-cookie");

        HttpServletRequest request = mock(HttpServletRequest.class);

        when(WebUtils.getCookie(request, "refresh-jwt-cookie")).thenReturn(null);

        String token = refreshTokenService.getRefreshTokenFromCookies(request);

        assertThat(token).isNull();
    }

    @Test
    void deleteByToken_WhenTokenExists_ShouldDeleteRefreshToken() {
        when(refreshTokenRepository.findByToken(TEST_REFRESH_TOKEN)).thenReturn(Optional.of(refreshTokenExpected));

        refreshTokenService.deleteByToken(TEST_REFRESH_TOKEN);

        verify(refreshTokenRepository).findByToken(TEST_REFRESH_TOKEN);
        verify(refreshTokenRepository).delete(refreshTokenExpected);
    }

    @Test
    void deleteByToken_WhenTokenDoesNotExist_ShouldThrowTokenException() {
        when(refreshTokenRepository.findByToken(TEST_REFRESH_TOKEN)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> refreshTokenService.deleteByToken(TEST_REFRESH_TOKEN))
                .isInstanceOf(TokenException.class)
                .hasMessageContaining("Refresh token does not exist.");
    }

    @Test
    void getCleanRefreshTokenCookie_ShouldReturnEmptyCookie() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenName", "refresh-jwt-cookie");

        ResponseCookie cookie = refreshTokenService.getCleanRefreshTokenCookie();

        assertThat(cookie).isNotNull()
                .satisfies(actualCookie -> {
                    assertThat(actualCookie.getName()).isEqualTo("refresh-jwt-cookie");
                    assertThat(actualCookie.getValue()).isEmpty();
                    assertThat(actualCookie.getPath()).isEqualTo("/");
                });
    }
}



















