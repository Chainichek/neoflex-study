package ru.chainichek.neostudy.deal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.chainichek.neostudy.deal.dto.util.ErrorMessage;
import ru.chainichek.neostudy.deal.util.RestResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class AuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER_NAME = "X-API-KEY";

    private final AuthProvider authProvider;
    private final SecuredUri securedUri;

    private final ObjectMapper objectMapper;
    private final RestResponseEntityExceptionHandler exceptionHandler;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authToken = request.getHeader(AUTH_HEADER_NAME);

        logger.info("Incoming request on secured uri: method = %s,  requestURI = %s, authToken = %s".formatted(request.getMethod(),
                request.getRequestURI(),
                authToken)
        );

        if (authToken == null) {
            logger.warn("Unauthorized request to %s: Missing X-API-KEY header".formatted(request.getRequestURI()));

            final ResponseEntity<ErrorMessage> responseEntity = exceptionHandler.unauthorizedException("Unauthorized request on secured URI",
                    request);

            response.setStatus(responseEntity.getStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().write(objectMapper.writeValueAsBytes(responseEntity.getBody()));

            return;
        }

        try {
            final Authentication authentication = authProvider.getAuthentication(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            logger.warn("Forbidden request to %s: X-API-KEY is invalid".formatted(request.getRequestURI()));

            final ResponseEntity<ErrorMessage> responseEntity = exceptionHandler.badCredentialsException(e, request);

            response.setStatus(responseEntity.getStatusCode().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().write(objectMapper.writeValueAsBytes(responseEntity.getBody()));

            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        return securedUri.getUris().stream()
                .noneMatch(uriPattern -> Pattern.matches(uriPattern.replace("**", ".*"), request.getRequestURI()));
    }


}