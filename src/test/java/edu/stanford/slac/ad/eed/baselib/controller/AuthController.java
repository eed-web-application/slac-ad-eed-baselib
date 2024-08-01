package edu.stanford.slac.ad.eed.baselib.controller;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationDTO;
import edu.stanford.slac.ad.eed.baselib.auth.jwt.SLACAuthenticationJWTToken;
import edu.stanford.slac.ad.eed.baselib.controller.dto.TokenTesterInfo;
import edu.stanford.slac.ad.eed.baselib.exception.NotAuthorized;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;


@RestController()
@RequestMapping("/v1/auth/test")
public class AuthController {
    @GetMapping(
            path = "/token",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public TokenTesterInfo getAuthenticationToken(Authentication authentication) {
        if(authentication == null) {
            throw  NotAuthorized.notAuthorizedBuilder().build();
        } else if(!authentication.getClass().isAssignableFrom(SLACAuthenticationJWTToken.class)) {
            throw NotAuthorized.notAuthorizedBuilder().build();
        }
        var auth = (SLACAuthenticationJWTToken)authentication;
        TokenTesterInfo result = new TokenTesterInfo();
        result.isAuthenticated = auth.isAuthenticated();
        result.isImpersonating = auth.isImpersonating();
        result.principal = auth.getPrincipal().toString();
        result.credential = auth.getCredentials().toString();
        result.sourcePrincipal = auth.getSourcePrincipal().toString();
        result.authorizationDTOS = auth.getAuthorities().stream()
                .filter(a -> a instanceof AuthorizationDTO)
                .map(a -> (AuthorizationDTO) a)
                .collect(Collectors.toList());
        return result;
    }
}
