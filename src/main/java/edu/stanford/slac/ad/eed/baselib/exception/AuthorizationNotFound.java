package edu.stanford.slac.ad.eed.baselib.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static edu.stanford.slac.ad.eed.baselib.exception.Utility.getAllMethodInCall;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Authorization has not been found")
public class AuthorizationNotFound extends ControllerLogicException {
    @Builder(builderMethodName = "authorizationNotFound")
    public AuthorizationNotFound(Integer errorCode, String authId) {
        super(errorCode,
                String.format("The authorization with id '%s' has not been found", authId),
                getAllMethodInCall()
        );
    }
}
