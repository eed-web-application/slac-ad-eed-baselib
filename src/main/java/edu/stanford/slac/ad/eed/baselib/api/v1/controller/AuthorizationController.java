package edu.stanford.slac.ad.eed.baselib.api.v1.controller;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.*;
import edu.stanford.slac.ad.eed.baselib.exception.NotAuthorized;
import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import edu.stanford.slac.ad.eed.baselib.service.PeopleGroupService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    //@Cacheable(value = "current-user-info", key = "#authentication.credentials")
    public ApiResultResponse<PersonDTO> me(Authentication authentication) {
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
                peopleGroupService.findPerson(
                        authentication
                )
        );
    }

    @GetMapping(
            path = "/users",
            produces = {MediaType.APPLICATION_JSON_VALUE}
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
    public ApiResultResponse<AuthenticationTokenDTO> createNewAuthenticationToken(
            Authentication authentication,
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
    public ApiResultResponse<List<AuthenticationTokenDTO>> getApplicationAuthenticationToken(
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
    public ApiResultResponse<Boolean> deleteAuthenticationToken(
            @Parameter(description = "Is the unique eid of the authentication token")
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
