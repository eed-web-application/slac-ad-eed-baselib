package edu.stanford.slac.ad.eed.baselib.auth.jwt;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationOwnerTypeDTO;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.auth.BaseSignKeyResolver;
import edu.stanford.slac.ad.eed.baselib.exception.NotAuthorized;
import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
        if (authentication.getPrincipal() == null) {
            return new SLACAuthenticationJWTToken();
        }
        try {

            SLACAuthenticationJWTToken resultToken = null;
            SLACAuthenticationToken slacToken = (SLACAuthenticationToken) authentication;
            boolean isImpersonating = slacToken.getImpersonateUserID()!=null && !slacToken.getImpersonateUserID().isEmpty();

            Jws<Claims> j = Jwts.parser()
                    .setSigningKeyResolver(signKeyResolver)
                    .build()
                    .parseClaimsJws(((SLACAuthenticationToken) authentication).getUserToken());

            Claims tokenClaims = j.getBody();
            if (!tokenClaims.containsKey("email")) {
                throw new BadCredentialsException("The 'email' is not present in the claims of the jwt");
            }
            String authorizationUserEmail = tokenClaims.get("email").toString();
            if(isImpersonating) {
                log.warn("User '{}' is trying to impersonate '{}'", authorizationUserEmail, slacToken.getImpersonateUserID());
                // check if user is authorized
                if(authService.checkForRoot(authorizationUserEmail)){
                    // change the user id to impersonate
                    authorizationUserEmail =  slacToken.getImpersonateUserID();
                    // Create a new Claims instance and copy existing claims to it
                    HashMap<String, Object> newClaims = new HashMap<>(tokenClaims);
                    newClaims.put("imp-email", authorizationUserEmail);
                    tokenClaims = Jwts.claims(newClaims);
                } else {
                    log.warn("User '{}' is trying to impersonate '{}', but it is not authorized", authorizationUserEmail, slacToken.getImpersonateUserID());
                    throw NotAuthorized
                            .notAuthorizedBuilder()
                            .errorCode(-1)
                            .errorDomain("SLACAuthenticationProvider::authenticate")
                            .build();
                }
            }
            if (appProperties.getAutoloadUserAuthorizations()) {
                // load all the authorization claims and create the token
                resultToken = SLACAuthenticationJWTToken.authenticated()
                        .tokenClaims(tokenClaims)
                        .authorities
                                (
                                        // load all user authorization
                                        authService.getAllAuthenticationForOwner
                                                (
                                                        authorizationUserEmail,
                                                        // check if we have a user or a token
                                                        appProperties.isAuthenticationToken(authorizationUserEmail) ?
                                                                AuthorizationOwnerTypeDTO.Token :
                                                                AuthorizationOwnerTypeDTO.User,
                                                        Optional.of(false)
                                                )
                                )
                        .build();
            } else {
                resultToken = new SLACAuthenticationJWTToken(tokenClaims, Collections.emptyList());
            }
            if(isImpersonating){
                log.info("User '{}' is impersonating '{}'", resultToken.getSourcePrincipal(), resultToken.getPrincipal());
            }
            return resultToken;
        } catch (Throwable e) {
            log.error("{}", e.toString());
            throw new BadCredentialsException("Invalid token signature");
        }

    }
}
