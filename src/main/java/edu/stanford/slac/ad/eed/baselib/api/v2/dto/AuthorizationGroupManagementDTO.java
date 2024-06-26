package edu.stanford.slac.ad.eed.baselib.api.v2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Group management authorization")
public record AuthorizationGroupManagementDTO(
        @Schema(description = "List of the user to add as authorized to manage group")
        List<String> addUsers,
        @Schema(description = "List of the user to remove as authorized to manage group")
        List<String> removeUsers
){}