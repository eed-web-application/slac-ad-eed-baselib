package edu.stanford.slac.ad.eed.baselib.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Person Query Parameter DTO")
public record PersonQueryParameterDTO(
        @Schema(description = "The last user id found in previous page")
        String anchor,
        @Schema(description = "The limit size of the search")
        Integer limit,
        @Schema(description = "The context size of the search")
        Integer context,
        @Schema(description = "The search filter")
        String searchFilter
) {
}
