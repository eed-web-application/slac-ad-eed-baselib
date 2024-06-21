package edu.stanford.slac.ad.eed.baselib.api.v2.controller;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.*;
import edu.stanford.slac.ad.eed.baselib.api.v2.dto.LocalGroupDTO;
import edu.stanford.slac.ad.eed.baselib.api.v2.dto.LocalGroupQueryParameterDTO;
import edu.stanford.slac.ad.eed.baselib.api.v2.dto.NewLocalGroupDTO;
import edu.stanford.slac.ad.eed.baselib.api.v2.dto.UpdateLocalGroupDTO;
import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import edu.stanford.slac.ad.eed.baselib.service.PeopleGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Validated
@RestController()
@RequestMapping("/v2/auth/local/group")
@AllArgsConstructor
@Schema(description = "Api for authentication information")
public class AuthorizationController {
    AuthService authService;
    PeopleGroupService peopleGroupService;


    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "Create new local group"
    )
    @PreAuthorize("@authService.canManageGroup(#authentication)")
    public ApiResultResponse<String> createNewLocalGroup(
            Authentication authentication,
            @RequestBody @Valid NewLocalGroupDTO newGroupDTO
            ) {
        return ApiResultResponse.of(
                authService.createLocalGroup(newGroupDTO)
        );
    }

    @DeleteMapping(
            path = "/{localGroupId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "Delete a local group"
    )
    @PreAuthorize("@authService.canManageGroup(#authentication)")
    public ApiResultResponse<Boolean> deleteLocalGroup(
            Authentication authentication,
            @Parameter(description = "The id of the local group to delete")
            @PathVariable @NotEmpty String localGroupId
    ) {
        // check authentication
        authService.deleteLocalGroup(localGroupId);
        return ApiResultResponse.of(true);
    }

    @PutMapping(
            path = "/{localGroupId}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "Delete a local group"
    )
    @PreAuthorize("@authService.canManageGroup(#authentication)")
    public ApiResultResponse<Boolean> updateLocalGroup(
            Authentication authentication,
            @Parameter(description = "The id of the local group to delete")
            @PathVariable @NotEmpty String localGroupId,
            @RequestBody @Valid UpdateLocalGroupDTO updateGroupDTO
    ) {
        // check authentication
        authService.updateLocalGroup(localGroupId, updateGroupDTO);
        return ApiResultResponse.of(true);
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "Get current user information"
    )
    @PreAuthorize("@authService.canManageGroup(#authentication)")
    public ApiResultResponse<List<LocalGroupDTO>> findLocalGroup(
            Authentication authentication,
            @Parameter(name = "anchorId", description = "Is the id of an entry from where start the search")
            @RequestParam("anchorId") Optional<String> anchorId,
            @Parameter(name = "contextSize", description = "Include this number of entries before the startDate (used for highlighting entries)")
            @RequestParam("contextSize") Optional<Integer> contextSize,
            @Parameter(name = "limit", description = "Limit the number the number of entries after the start date.")
            @RequestParam(value = "limit") Optional<Integer> limit,
            @Parameter(name = "search", description = "Typical search functionality")
            @RequestParam(value = "search") Optional<String> search
    ) {
        return ApiResultResponse.of(
                authService.findLocalGroup(
                        LocalGroupQueryParameterDTO.builder()
                                .anchorID(anchorId.orElse(null))
                                .contextSize(contextSize.orElse(null))
                                .limit(limit.orElse(null))
                                .search(search.orElse(null))
                                .build()
                )
        );
    }


}
