package com.voedev.finance.service;

import com.voedev.finance.model.entity.RefreshToken;
import com.voedev.finance.model.entity.User;
import com.voedev.finance.model.enums.user.UserRole;
import com.voedev.finance.model.enums.user.UserStatus;
import com.voedev.finance.repository.RefreshTokenRepository;
import com.voedev.finance.repository.UserRepository;
import com.voedev.finance.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

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
    private User userEntity;
    private final Integer refreshTokenExpiration = 1296000000;

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
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
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
                            Instant.now().plusMillis(refreshTokenExpiration), within(5, ChronoUnit.SECONDS));
                    assertThat(actual.isRevoked()).isFalse();
                });

        verify(userRepository).findById(refreshTokenExpected.getUser().getId());
        verify(refreshTokenRepository).save(any());
    }


}



















