package edu.stanford.salc.slacadeedbaselib.exception;

import edu.stanford.salc.slacadeedbaselib.api.v1.dto.ApiResultResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "ControllerLogicException")
public class ControllerLogicException extends RuntimeException {
    private int errorCode;
    private String errorMessage;
    private String errorDomain;
    public ApiResultResponse<Object> toApiResultResponse() {
        return ApiResultResponse.of(this.errorCode, this.errorMessage, this.errorDomain);
    }

    /**
     * Convert an exception to the default DTO
     * @param logicException the exception to convert
     * @return a dto that represent the exception
     * @param <T> the type of the returned payload
     */
    static public <T> ApiResultResponse<T> toApiResultResponse(ControllerLogicException logicException) {
        return ApiResultResponse.<T>builder()
                .errorCode(logicException.getErrorCode())
                .errorMessage(logicException.getErrorMessage())
                .errorDomain(logicException.getErrorDomain())
                .build();
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorDomain(String errorDomain) {
        this.errorDomain = errorDomain;
    }

    static public ControllerLogicException of(int errorCode, String errorMessage, String errorDomain) {
        return new ControllerLogicException(errorCode, errorMessage, errorDomain);
    }

    static public ControllerLogicException of(int errorCode, String errorMessage, String errorDomain, Throwable cause) {
        return new ControllerLogicException(errorCode, errorMessage, errorDomain);
    }
}
