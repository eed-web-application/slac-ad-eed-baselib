package edu.stanford.slac.ad.eed.baselib.api.v1.controller;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.*;
import edu.stanford.slac.ad.eed.baselib.exception.NotAuthorized;
import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import edu.stanford.slac.ad.eed.baselib.service.PeopleGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static edu.stanford.slac.ad.eed.baselib.exception.Utility.assertion;


@Log4j2
@RestController()
@RequestMapping("/v1/auth")
@AllArgsConstructor
@Schema(description = "Api for authentication information")
public class AuthorizationController {
    AuthService authService;
    PeopleGroupService peopleGroupService;


    @GetMapping(
            path = "/me",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "Get current user information"
    )
    @Cacheable(value = "current-user-info", key = "#authentication.credentials")
    public ApiResultResponse<PersonDetailsDTO> me(Authentication authentication) {
        // check authentication
        assertion(
                () -> authService.checkAuthentication(authentication),
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("AuthorizationController::me")
                        .build()
        );
        return ApiResultResponse.of(
                PersonDetailsDTO.builder()
                        .person(
                                peopleGroupService.findPerson(
                                        authentication
                                )
                        )
                        .authorizations(
                                authService.getAllAuthenticationForOwner(
                                        authentication.getPrincipal().toString(),
                                        AuthorizationOwnerTypeDTO.User,
                                        Optional.empty()
                                )
                        )
                        .build()

        );
    }

    @GetMapping(
            path = "/users",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "Find within user list"
    )
    public ApiResultResponse<List<PersonDTO>> findPeople(
            @Parameter(description = "is the prefix used to filter the people")
            @RequestParam() Optional<String> search,
            Authentication authentication
    ) {
        // check authenticated
        assertion(
                () -> authService.checkAuthentication(authentication),
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("LogbooksController::updateShift")
                        .build()
        );
        // assert that all the user that are root or admin of whatever resource
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("AuthorizationController::findPeople")
                        .build(),
                () -> authService.checkAuthorizationForOwnerAuthTypeAndResourcePrefix(
                        authentication,
                        AuthorizationTypeDTO.Admin,
                        "/"
                )
        );
        return ApiResultResponse.of(
                peopleGroupService.findPersons(
                        search.orElse("")
                )
        );
    }

    @GetMapping(
            path = "/groups",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(
            summary = "Find within user group"
    )
    public ApiResultResponse<List<GroupDTO>> findGroups(
            @Parameter(description = "is the prefix used to filter the groups")
            @RequestParam() Optional<String> search,
            Authentication authentication
    ) {
        // check authenticated
        assertion(
                () -> authService.checkAuthentication(authentication),
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("LogbooksController::updateShift")
                        .build()
        );
        // assert that all the user that are root or admin of whatever resource
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("AuthorizationController::findPeople")
                        .build(),
                () -> authService.checkAuthorizationForOwnerAuthTypeAndResourcePrefix(
                        authentication,
                        AuthorizationTypeDTO.Admin,
                        "/"
                )

        );
        return ApiResultResponse.of(
                peopleGroupService.findGroup(
                        search.orElse("")
                )
        );
    }

    /**
     * Create root user
     */
    @PostMapping(
            path = "/root/{email}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create root user authorization", description = "Let the user, identified by the email, to be root")
    public ApiResultResponse<Boolean> setRootUser(
            @PathVariable String email,
            Authentication authentication
    ) {
        // assert that all the user that are root of whatever resource
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-2)
                        .errorDomain("AuthorizationController::setRootUser")
                        .build(),
                // is authenticated
                () -> authService.checkAuthentication(authentication),
                // is admin
                () -> authService.checkForRoot(
                        authentication
                )
        );
        authService.addRootAuthorization(email, authentication.getCredentials().toString());
        return ApiResultResponse.of(
                true
        );
    }

    /**
     * Delete root user
     */
    @DeleteMapping(
            path = "/root/{email}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(summary = "Delete root user authorization", description = "Remove the user, identified by the email, to be root")
    public ApiResultResponse<Boolean> removeAsRootUser(
            @PathVariable String email,
            Authentication authentication
    ) {
        // assert that all the user that are root of whatever resource
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-2)
                        .errorDomain("AuthorizationController::setRootUser")
                        .build(),
                // is authenticated
                () -> authService.checkAuthentication(authentication),
                // is admin
                () -> authService.checkForRoot(
                        authentication
                )
        );
        authService.removeRootAuthorization(email);
        return ApiResultResponse.of(
                true
        );
    }

    /**
     * Delete root user
     */
    @GetMapping(
            path = "/root",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(summary = "Delete root user authorization", description = "Remove the user, identified by the email, to be root")
    public ApiResultResponse<List<AuthorizationDTO>> findAllRoot(
            Authentication authentication
    ) {
        // assert that all the user that are root of whatever resource
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("AuthorizationController::setRootUser")
                        .build(),
                // is authenticated
                () -> authService.checkAuthentication(authentication),
                // is admin
                () -> authService.checkForRoot(
                        authentication
                )
        );

        return ApiResultResponse.of(
                authService.findAllRoot()
        );
    }

    /**
     * return all the application token
     */
    @PostMapping(
            path = "/application-token",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create new authentication token",
            description = """
                    Create a new authentication token, a token permit to access the application without the needs of a user password
                    it should be submitted in the http header along with the http request
                    """
    )
    public ApiResultResponse<AuthenticationTokenDTO> createNewAuthenticationToken(
            Authentication authentication,
            @Parameter(description = "Is the new authentication token to be created")
            @RequestBody NewAuthenticationTokenDTO newAuthenticationTokenDTO
    ) {
        // assert that all the user that are root of whatever resource
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("AuthorizationController::createNewAuthenticationToken")
                        .build(),
                // is authenticated
                () -> authService.checkAuthentication(authentication),
                // is admin
                () -> authService.checkForRoot(
                        authentication
                )
        );
        return ApiResultResponse.of(
                authService.addNewApplicationAuthenticationToken(newAuthenticationTokenDTO, false)
        );
    }

    /**
     * return all the application token
     */
    @GetMapping(
            path = "/application-token",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Find all authentication token"
    )
    public ApiResultResponse<List<AuthenticationTokenDTO>> findAllAuthenticationToken(
            Authentication authentication
    ) {
        // assert that all the user that are root of whatever resource
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("AuthorizationController::getAuthenticationToken")
                        .build(),
                // is authenticated
                () -> authService.checkAuthentication(authentication),
                // is admin
                () -> authService.checkForRoot(
                        authentication
                )

        );
        return ApiResultResponse.of(
                authService.getAllAuthenticationToken()
        );
    }

    /**
     * return all the application token
     */
    @DeleteMapping(
            path = "/application-token/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete an authentication token"
    )
    public ApiResultResponse<Boolean> deleteAuthenticationToken(
            @Parameter(description = "Is the unique id of the authentication token")
            @PathVariable() String id,
            Authentication authentication
    ) {
        // assert that all the user that are root of whatever resource
        assertion(
                NotAuthorized
                        .notAuthorizedBuilder()
                        .errorCode(-1)
                        .errorDomain("AuthorizationController::deleteAuthenticationToken")
                        .build(),
                // is authenticated
                () -> authService.checkAuthentication(authentication),
                // is admin
                () -> authService.checkForRoot(
                        authentication
                )

        );
        authService.deleteToken(id);
        return ApiResultResponse.of(true);
    }
}
