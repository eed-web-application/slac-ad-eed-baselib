package edu.stanford.slac.ad.eed.baselib.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Is the description for an authorization")
public record NewAuthorizationDTO(
        @Schema(description = "Is the type of the authorizations [User, Group, Application]")
        AuthorizationLevelDTO authorizationType,
        @Schema(description = "Is the subject owner of the authorizations")
        String owner,
        @Schema(description = "Is the type of the owner [User, Group, Application]")
        AuthorizationOwnerTypeDTO ownerType,
        @Schema(description = "The resource eof the authorizations")
        String resource
)  implements Serializable {
}
