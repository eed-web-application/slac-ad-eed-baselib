package edu.stanford.slac.ad.eed.baselib.auth;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import lombok.extern.log4j.Log4j2;

import java.security.Key;

@Log4j2
public  abstract class BaseSignKeyResolver extends SigningKeyResolverAdapter {
    private final JWTHelper jwtHelper;
    private OIDCConfiguration oidcConfiguration;

    public BaseSignKeyResolver(JWTHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        if (
                claims.containsKey(Claims.ISSUER) &&
                        claims.get(Claims.ISSUER).toString().compareToIgnoreCase(
                                jwtHelper.getApplicationIssuer()
                        ) == 0
        ) {
            Key result = null;
            if (claims.containsKey(Claims.SUBJECT)) {
                log.debug("Validate jwt token for logbook token application: %s".formatted(claims.get(Claims.SUBJECT)));
                // the key is the application signing key
                result = jwtHelper.getKey();
            }
           return result;
        }
        return null;

    }
}
