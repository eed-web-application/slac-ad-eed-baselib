package edu.stanford.slac.ad.eed.baselib.auth.jwt;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationTypeDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collection;
import java.util.Collections;

@Getter
/**
 * SLAC Authentication JWT Token
 */
public class SLACAuthenticationJWTToken extends AbstractAuthenticationToken {
    private Jws<Claims> token = null;

    /**
     * Constructor
     */
    public SLACAuthenticationJWTToken() {
        super(Collections.emptyList());
        super.setAuthenticated(false);
    }

    /**
     * Constructor
     * @param token the token
     * @param authorities the authorities
     */
    @Builder(builderMethodName = "authenticated")
    public SLACAuthenticationJWTToken(Jws<Claims> token, Collection<AuthorizationDTO> authorities) {
        super(authorities);
        super.setAuthenticated(true);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token!=null?token.getBody().get("email"):null;
    }

    @Override
    public Object getPrincipal() {
        return token!=null?token.getBody().get("email"):null;
    }

    /**
     * Check if the user has authorization on a given resource
     * @param authorizationType the authorization type
     * @param resourcePrefix the resource prefix
     * @return true if the user has authorization on the resource
     */
    public boolean hasAuthorizationOn(AuthorizationTypeDTO authorizationType, String resourcePrefix) {
        if(getAuthorities() == null) return false;
        return getAuthorities()
                .stream()
                .map(a->(AuthorizationDTO)a)
                // check if authorization is great or equal to actual
                .filter(a->decode(authorizationType) <= decode(a.authorizationType()))
                .filter(a->a.resource().startsWith(resourcePrefix))
                .findAny().isPresent();
    }

    /**
     * Check if the user is an admin
     * @return true if the user has authorization on the resource
     */
    public boolean isRoot() {
        if(getAuthorities() == null) return false;
        return getAuthorities()
                .stream()
                .map(a->(AuthorizationDTO)a)
                .filter(a->a.authorizationType() == AuthorizationTypeDTO.Admin)
                .filter(a->a.resource().equals("*"))
                .findAny().isPresent();
    }

    /**
     * Decode the authorization type
     * @param authorizationType the authorization type
     * @return the decoded authorization type
     */
    private int decode(AuthorizationTypeDTO authorizationType) {
        switch (authorizationType) {
            case AuthorizationTypeDTO.Read:
                return 1;
            case AuthorizationTypeDTO.Write:
                return 2;
            case AuthorizationTypeDTO.Admin:
                return 3;
            default:
                return 0;
        }
    }
}
