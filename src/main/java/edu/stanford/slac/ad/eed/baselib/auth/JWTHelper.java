package edu.stanford.slac.ad.eed.baselib.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.exception.ControllerLogicException;
import edu.stanford.slac.ad.eed.baselib.model.AuthenticationToken;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Log4j2
@Service
@AllArgsConstructor
public class JWTHelper {
    private final AppProperties appProperties;

    private static Key secretKey = null;
    private static final long EXPIRATION_TIME_MS = 3600000;

    public String getApplicationIssuer() {
        return appProperties.getAppName();
    }

    // For use with MockMvc
    public String generateJwt(String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME_MS);
        Map<String,Object> claims = new HashMap<>();
        claims.put("email", email);
        // Build the JWT
        return Jwts.builder()
                .setHeader(
                        Map.of(Header.TYPE, Header.JWT_TYPE)
                )
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getKey())
                .compact();
    }

    /**
     * Generate a service token

     * @return the service token
     */
    public String generateServiceToken(){
        return generateAuthenticationToken(
                AuthenticationToken.builder()
                        .name(appProperties.getAppName())
                        .email("service@internal.%s".formatted(appProperties.getAppEmailPostfix()))
                        .expiration(
                                LocalDate.now().plusDays(365)

                        )
                        .build()
        );
    }

    public String generateAuthenticationToken(AuthenticationToken authenticationToken) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("email", authenticationToken.getEmail());
        // Build the JWT
        return Jwts.builder()
                .setHeader(
                        Map.of(Header.TYPE, Header.JWT_TYPE)
                )
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setIssuer(appProperties.getAppName())
                .setSubject(authenticationToken.getName())
                .setExpiration(
                        Date.from(
                                authenticationToken.getExpiration().atStartOfDay().toInstant(ZoneOffset.UTC)
                        )
                )
                .signWith(getKey())
                .compact();
    }

    public Key getKey() {
        if(secretKey==null) {
            if(appProperties.getAppTokenJwtKey() == null) {
                throw ControllerLogicException.of(
                        -1,
                        "The app token key is null",
                        "JWTHelper::getKey"
                );
            }
            log.debug("Using key of size '{}' for sign authentication token JWT", appProperties.getAppTokenJwtKey().length());
            byte[] keyBytes = hexStringToByteArray(appProperties.getAppTokenJwtKey());
            secretKey = Keys.hmacShaKeyFor(keyBytes);
        }
        return secretKey;
    }

    private byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}