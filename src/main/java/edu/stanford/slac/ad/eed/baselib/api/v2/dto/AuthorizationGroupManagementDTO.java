package edu.stanford.slac.ad.eed.baselib.api.v2.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Group management authorization")
public record AuthorizationGroupManagementDTO(
        @Schema(description = "List of the user to add as authorized to manage group")
        List<String> addUsers,
        @Schema(description = "List of the user to remove as authorized to manage group")
        List<String> removeUsers
){}