package edu.stanford.slac.ad.eed.baselib.auth.jwt;

import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.auth.BaseSignKeyResolver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Scope("prototype")
@AllArgsConstructor
public class SLACAuthenticationProvider implements AuthenticationProvider {
    private final AppProperties appProperties;
    private final BaseSignKeyResolver signKeyResolver;

    @Override
    public boolean supports(Class<?> authentication) {
        return SLACAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
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
//            if (!jwtBody.containsKey("name")) {
//                throw new BadCredentialsException("The 'name' is not present in the claims of the jwt");
//            }
            return new SLACAuthenticationJWTToken(j);
        } catch (Throwable e) {
            log.error("{}", e.toString());
            throw new BadCredentialsException("Invalid token signature");
        }

    }
}
