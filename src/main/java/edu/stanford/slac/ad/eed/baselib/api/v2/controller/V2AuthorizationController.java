package edu.stanford.slac.ad.eed.baselib.api.v2.controller;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.*;
import edu.stanford.slac.ad.eed.baselib.api.v2.dto.*;
import edu.stanford.slac.ad.eed.baselib.exception.NotAuthorized;
import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import edu.stanford.slac.ad.eed.baselib.service.PeopleGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static edu.stanford.slac.ad.eed.baselib.exception.Utility.any;
import static edu.stanford.slac.ad.eed.baselib.exception.Utility.assertion;

@Validated
@RestController()
@RequestMapping("/v2/auth/local/group")
@AllArgsConstructor
@Schema(description = "Api for authentication information")
public class V2AuthorizationController {
    AuthService authService;
    PeopleGroupService peopleGroupService;


    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "Create new local group"
    )
    public ApiResultResponse<String> createNewLocalGroup(
            Authentication authentication,
            @RequestBody @Valid NewLocalGroupDTO newGroupDTO
    ) {
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("V2AuthorizationController::createNewLocalGroup")
                        .build(),
                // is authenticated
                () -> any
                        (
                                () -> authService.checkForRoot(authentication),
                                () -> authService.canManageGroup(authentication)
                        )
        );
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
    public ApiResultResponse<Boolean> deleteLocalGroup(
            Authentication authentication,
            @Parameter(description = "The id of the local group to delete")
            @PathVariable @NotEmpty String localGroupId
    ) {
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("V2AuthorizationController::deleteLocalGroup")
                        .build(),
                // is authenticated
                () -> any
                        (
                                () -> authService.checkForRoot(authentication),
                                () -> authService.canManageGroup(authentication)
                        )
        );
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
    public ApiResultResponse<Boolean> updateLocalGroup(
            Authentication authentication,
            @Parameter(description = "The id of the local group to delete")
            @PathVariable @NotEmpty String localGroupId,
            @RequestBody @Valid UpdateLocalGroupDTO updateGroupDTO
    ) {
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("V2AuthorizationController::updateLocalGroup")
                        .build(),
                // is authenticated
                () -> any
                        (
                                () -> authService.checkForRoot(authentication),
                                () -> authService.canManageGroup(authentication)
                        )
        );
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
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("V2AuthorizationController::updateLocalGroup")
                        .build(),
                // is authenticated
                () -> any
                        (
                                () -> authService.checkForRoot(authentication),
                                () -> authService.canManageGroup(authentication)
                        )
        );
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

    @PostMapping(
            path = "/authorize",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "Get current user information"
    )
    public ApiResultResponse<Boolean> manageGroupManagementAuthorization(
            Authentication authentication,
            @RequestBody @Valid AuthorizationGroupManagementDTO authorizationGroupManagementDTO

    ) {
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("V2AuthorizationController::manageGroupManagementAuthorization")
                        .build(),
                // is authenticated
                () -> any
                        (
                                () -> authService.checkForRoot(authentication),
                                () -> authService.canManageGroup(authentication)
                        )
        );
        authService.manageAuthorizationOnGroup(authorizationGroupManagementDTO);
        return ApiResultResponse.of(true);
    }

    @GetMapping(
            path = "/authorize/{userIds}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "Get current user authorization on group management"
    )
    public ApiResultResponse<List<UserGroupManagementAuthorizationLevel>> getGroupManagementAuthorization(
            Authentication authentication,
            @Parameter(
                    description = "The id of the local group to delete, each id should be a comma separated list",
                    example = "user1@slac.stanford.edu,user2@slac.stanford.edu")
            @PathVariable @Valid List<String> userIds

    ) {
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("V2AuthorizationController::getGroupManagementAuthorization")
                        .build(),
                // is authenticated
                () -> any
                        (
                                () -> authService.checkForRoot(authentication),
                                () -> authService.canManageGroup(authentication)
                        )
        );
        return ApiResultResponse.of(authService.getGroupManagementAuthorization(userIds));
    }
}
