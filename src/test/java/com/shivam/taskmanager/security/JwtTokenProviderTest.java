package com.shivam.taskmanager.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtTokenProvider.
 */
class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private final String secret = "ThisIsASecretKeyForJwtTokenGenerationAndItShouldBeLongEnoughForHS256AlgorithmToWorkProperly";

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(secret, 86400000);
    }

    @Test
    void generateTokenFromUsername_shouldReturnValidToken() {
        String token = tokenProvider.generateTokenFromUsername("testuser", Set.of("USER"));
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        String token = tokenProvider.generateTokenFromUsername("testuser", Set.of("USER"));
        String extracted = tokenProvider.getUsernameFromToken(token);
        assertEquals("testuser", extracted);
    }

    @Test
    void validateToken_validToken_shouldReturnTrue() {
        String token = tokenProvider.generateTokenFromUsername("testuser", Set.of("USER"));
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();
        assertTrue(tokenProvider.validateToken(token, userDetails));
    }

    @Test
    void validateToken_wrongUser_shouldReturnFalse() {
        String token = tokenProvider.generateTokenFromUsername("user1", Set.of("USER"));
        UserDetails userDetails = User.builder()
                .username("user2")
                .password("password")
                .roles("USER")
                .build();
        assertFalse(tokenProvider.validateToken(token, userDetails));
    }

    @Test
    void validateToken_invalidToken_shouldReturnFalse() {
        UserDetails userDetails = User.builder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();
        assertFalse(tokenProvider.validateToken("invalid.token.here", userDetails));
    }
}