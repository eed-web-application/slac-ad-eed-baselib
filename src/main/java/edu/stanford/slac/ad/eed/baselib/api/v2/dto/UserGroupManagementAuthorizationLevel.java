package edu.stanford.slac.ad.eed.baselib.api.v2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.PersonDTO;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Builder;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserGroupManagementAuthorizationLevel(
        @Parameter(description = "The user to check authorization level")
        PersonDTO user,
        @Parameter(description = "The authorization level of the user")
        Boolean canManageGroup
) {
}
