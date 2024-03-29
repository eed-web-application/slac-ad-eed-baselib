package edu.stanford.slac.ad.eed.baselib.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResultResponse<T> {
    @Builder.Default
    @Schema(description = "Is the error code returned from api", requiredMode = Schema.RequiredMode.REQUIRED)
    private int errorCode = 0;
    @Schema(description = "In case of error not equal to 0, an error message can be reported by api, indicating what problem is occurred")
    private String errorMessage;
    @Schema(description = "In case of error not equal to 0, an error domain can be reported by api, indicating where the problem is occurred")
    private String errorDomain;
    @Schema(description = "Is the value returned by api")
    private T payload;

    /**
     * Fast constructor for error situation
     *
     * @param errorCode    the error code
     * @param errorMessage the error message
     * @param errorDomain  the error domain, where the error is occurs
     * @param <T>          the type
     * @return return the instance with error information
     */
    public static <T> ApiResultResponse<T> of(
            int errorCode,
            String errorMessage,
            String errorDomain) {
        return ApiResultResponse
                .<T>builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .errorDomain(errorDomain)
                .build();
    }

    /**
     * Fast constructor for good result situation
     *
     * @param payload the payload of the result
     * @param <T>     the generics type
     * @return return the instance with payload
     */
    public static <T> ApiResultResponse<T> of(T payload) {
        return ApiResultResponse
                .<T>builder()
                .payload(payload)
                .build();
    }
}
