package edu.stanford.slac.ad.eed.baselib.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

@Getter
public class SLACAuthenticationJWTToken extends AbstractAuthenticationToken {
    private Jws<Claims> token = null;
    public SLACAuthenticationJWTToken() {
        super(Collections.emptyList());
        super.setAuthenticated(false);
    }

    public SLACAuthenticationJWTToken(Jws<Claims> token) {
        super(Collections.emptyList());
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
}
