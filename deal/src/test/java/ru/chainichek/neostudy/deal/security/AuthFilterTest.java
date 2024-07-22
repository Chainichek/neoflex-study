package ru.chainichek.neostudy.deal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import ru.chainichek.neostudy.deal.dto.util.ErrorMessage;
import ru.chainichek.neostudy.deal.util.RestResponseEntityExceptionHandler;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

    AuthFilter authFilter;
    AuthProvider authProvider;
    SecuredUri securedUri;
    RestResponseEntityExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        authProvider = mock(AuthProvider.class);
        securedUri = mock(SecuredUri.class);
        exceptionHandler = mock(RestResponseEntityExceptionHandler.class);

        authFilter = new AuthFilter(authProvider, securedUri, new ObjectMapper(), exceptionHandler);
    }

    @Test
    void doFilterInternal_whenNoAuthToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        ResponseEntity<ErrorMessage> responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mock(ErrorMessage.class));

        when(request.getHeader("X-API-KEY")).thenReturn(null);
        when(exceptionHandler.unauthorizedException(any(), any())).thenReturn(responseEntity);

        authFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void doFilterInternal_whenBadCredentials() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        ResponseEntity<ErrorMessage> responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).body(mock(ErrorMessage.class));

        when(request.getHeader("X-API-KEY")).thenReturn("invalid-token");
        when(authProvider.getAuthentication("invalid-token")).thenThrow(new BadCredentialsException("Invalid token"));
        when(exceptionHandler.badCredentialsException(any(), any())).thenReturn(responseEntity);

        authFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        verify(filterChain, never()).doFilter(any(), any());
    }

    @Test
    void doFilterInternal_whenTokenIsValid() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        Authentication authentication = mock(Authentication.class);

        when(request.getHeader("X-API-KEY")).thenReturn("valid-token");
        when(authProvider.getAuthentication("valid-token")).thenReturn(authentication);

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testShouldNotFilter() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/unsecured/uri");
        when(securedUri.getUris()).thenReturn(Collections.singletonList("/secured/**"));

        assertTrue(authFilter.shouldNotFilter(request));
    }
}
