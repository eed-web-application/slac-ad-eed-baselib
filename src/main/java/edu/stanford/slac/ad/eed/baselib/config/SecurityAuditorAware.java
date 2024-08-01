package edu.stanford.slac.ad.eed.baselib.config;

import edu.stanford.slac.ad.eed.baselib.auth.jwt.SLACAuthenticationJWTToken;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Optional<String> result = Optional.empty();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return result;
        } else if (authentication.getClass().isAssignableFrom(SLACAuthenticationJWTToken.class)) {
            // source principal always point to the real user
            return Optional.of(((SLACAuthenticationJWTToken) authentication).getSourcePrincipal().toString());
        }
        return Optional.of(authentication.getPrincipal().toString());
    }
}