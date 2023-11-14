package edu.stanford.salc.slacadeedbaselib.api.v1.controller;

import edu.stanford.salc.slacadeedbaselib.api.v1.dto.ApiResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/echo")
@Tag(name = "Echo Controller v1", description = "Service testing api")
public class EchoController {
    /**
     * Print the echo
     *
     * @param value
     * @return
     */
    @GetMapping( path="/test/{value}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(summary = "Example api that realize an ECHO")
    @ResponseStatus(HttpStatus.OK)
    public ApiResultResponse<String> echo(
            @Schema(description = "Is the value that will be returned in post operation",
                    pattern = "[a-zA-Z0-9]*",
                    requiredMode = Schema.RequiredMode.REQUIRED)
            @PathVariable("value") String value) {
        return ApiResultResponse.of(value);
    }
}
