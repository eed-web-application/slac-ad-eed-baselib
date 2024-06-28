package edu.stanford.slac.ad.eed.baselib.api.v1.controller;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.ApiResultResponse;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.ModelChangesHistoryDTO;
import edu.stanford.slac.ad.eed.baselib.service.ModelHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/model/history")
@Tag(name = "Model history REST API", description = "REST API for model history operations")
public class ModelHistoryController {
    ModelHistoryService modelHistoryService;

    @GetMapping(
            path = "/model/{modelId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return the list of changes for a model id")
    public ApiResultResponse<List<ModelChangesHistoryDTO>> getHistoryForAModelId(
            Authentication authentication,
            @PathVariable @NotEmpty String modelId
    ) {

        return ApiResultResponse.of(
                modelHistoryService.findChangesByModelId(modelId)
        );
    }
}
