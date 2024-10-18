package edu.stanford.slac.ad.eed.baselib.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
@Builder
public class SLACAuthenticationToken extends AbstractAuthenticationToken {
    @Builder.Default
    private String userToken = null;
    @Builder.Default
    private String impersonateUserID = null;
    public SLACAuthenticationToken() {
        super(Collections.emptyList());
        super.setAuthenticated(false);
    }

    public SLACAuthenticationToken(String userToken, String impersonateUserID) {
        super(Collections.emptyList());
        super.setAuthenticated(true);
        this.userToken = userToken;
        this.impersonateUserID = impersonateUserID;
    }

    public SLACAuthenticationToken(String userUniqueId, Object details, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setDetails(details);
        super.setAuthenticated(true);
        this.userToken = userUniqueId;
    }

    @Override
    public Object getCredentials() {
        return userToken;
    }

    @Override
    public Object getPrincipal() {
        return userToken;
    }
}
