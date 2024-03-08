package edu.stanford.slac.ad.eed.baselib.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "The authorization of the user on the resources returned in the payload")
public record AuthorizationResourceDTO(
        @Schema(description = "Is the field on the resource that is target of the authorization")
        String field,
        @Schema(description = "Is the type of the authorizations [Read, Write, Admin]")
        AuthorizationTypeDTO authorizationType
){}
