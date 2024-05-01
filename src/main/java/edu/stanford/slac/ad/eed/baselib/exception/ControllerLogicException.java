package edu.stanford.slac.ad.eed.baselib.exception;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.ApiResultResponse;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.ApiResultResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Builder
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "ControllerLogicException")
public class ControllerLogicException extends RuntimeException {
    private int errorCode;
    private String errorMessage;
    private String errorDomain;

    @Builder(builderMethodName = "builder")
    public ControllerLogicException(int errorCode, String errorMessage, String errorDomain) {
        super(errorMessage); // This will set the standard message field
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDomain = errorDomain;
    }
}
