package ru.chainichek.neostudy.deal.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider {
    @Value("${app.security.auth-token}")
    private String authToken;

    public Authentication getAuthentication(String authToken) {
        if (authToken == null || !authToken.equals(this.authToken)) {
            throw new BadCredentialsException("Invalid authentication token");
        }

        return new ApiKeyAuthentication(authToken, AuthorityUtils.NO_AUTHORITIES);
    }
}
