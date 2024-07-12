package edu.stanford.slac.ad.eed.baselib.auth.jwt;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationOwnerTypeDTO;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.auth.BaseSignKeyResolver;
import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Log4j2
@Component
@Scope("prototype")
@AllArgsConstructor
public class SLACAuthenticationProvider implements AuthenticationProvider {
    private final AppProperties appProperties;
    private final BaseSignKeyResolver signKeyResolver;
    private final AuthService authService;

    @Override
    public boolean supports(Class<?> authentication) {
        return SLACAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        AbstractAuthenticationToken token = null;
        if (authentication.getPrincipal() == null) {
            return new SLACAuthenticationJWTToken();
        }
        try {
            SLACAuthenticationToken slacToken = (SLACAuthenticationToken) authentication;

            Jws<Claims> j = Jwts.parser()
                    .setSigningKeyResolver(signKeyResolver)
                    .build()
                    .parseClaimsJws(((SLACAuthenticationToken) authentication).getUserToken());

            Claims jwtBody = j.getBody();
            if (!jwtBody.containsKey("email")) {
                throw new BadCredentialsException("The 'email' is not present in the claims of the jwt");
            }

            if (appProperties.getAutoloadUserAuthorizations()) {
                // load all the authorization claims and create the token
                token = SLACAuthenticationJWTToken.authenticated()
                        .token(j)
                        .authorities
                                (
                                        // load all user authorization
                                        authService.getAllAuthenticationForOwner
                                                (
                                                        jwtBody.get("email").toString(),
                                                        // check if we have an user or a token
                                                        appProperties.isAppTokenEmail(
                                                                jwtBody.get("email").toString()) ?
                                                                AuthorizationOwnerTypeDTO.Token :
                                                                AuthorizationOwnerTypeDTO.User,
                                                        Optional.of(false)
                                                )
                                )
                        .build();
            } else {
                token = new SLACAuthenticationJWTToken(j, Collections.emptyList());
            }
            return token;
        } catch (Throwable e) {
            log.error("{}", e.toString());
            throw new BadCredentialsException("Invalid token signature");
        }

    }
}
