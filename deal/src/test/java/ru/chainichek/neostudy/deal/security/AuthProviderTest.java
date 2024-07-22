package ru.chainichek.neostudy.deal.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AuthProviderTest {
    @InjectMocks
    AuthProvider authProvider;
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authProvider, "authToken", "12345");
    }

    @Test
    void getAuthentication_whenAuthTokenIsNull_throwBadCredentialsException() {
        assertThrows(BadCredentialsException.class, () -> authProvider.getAuthentication(null));
    }

    @Test
    void getAuthentication_whenAuthTokenIsNotEquals_throwBadCredentialsException() {
        assertThrows(BadCredentialsException.class, () -> authProvider.getAuthentication("00000"));
    }

    @Test
    void getAuthentication_wheAuthTokenIsNotNullAndEquals_doesNotThrow() {
        assertDoesNotThrow(() -> authProvider.getAuthentication("12345"));
    }
}