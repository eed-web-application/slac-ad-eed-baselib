package edu.stanford.slac.ad.eed.baselib.service;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.*;
import edu.stanford.slac.ad.eed.baselib.api.v2.dto.*;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.model.AuthenticationToken;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public abstract class AuthService {
    private final AppProperties appProperties;

    /**
     * Check if the current authentication is authenticated
     *
     * @param authentication the current authentication
     */
    public boolean checkAuthentication(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }


    public abstract List<AuthorizationDTO> findAllRoot();

    /**
     * Check if the current authentication is a root user
     *
     * @param authentication is the current authentication
     */
    @Cacheable(value = "user-authorization", key = "{#authentication.principal}", unless = "#authentication == null")
    public boolean checkForRoot(Authentication authentication) {
        if (!checkAuthentication(authentication)) return false;
        // only root user can create logbook
        List<AuthorizationDTO> foundAuth = getAllAuthorizationForOwnerAndAndAuthTypeAndResourcePrefix(
                authentication.getCredentials().toString(),
                AuthorizationTypeDTO.Admin,
                "*",
                Optional.empty()
        );
        return foundAuth != null && !foundAuth.isEmpty();
    }

    /**
     * Check the authorizations level on a resource, the authorizations found
     * will be all those authorizations that will have the value of authorizations type greater
     * or equal to the one give as argument. This return true also if the current authentication
     * is an admin. The api try to search if the user is authorized using user, groups or application checks
     *
     * @param authorization  the minimum value of authorizations to check
     * @param authentication the current authentication
     * @param resourcePrefix the target resource
     */
    @Cacheable(value = "user-authorization", key = "{#authentication.principal, #authorization, #resourcePrefix}", unless = "#authentication == null")
    public boolean checkAuthorizationForOwnerAuthTypeAndResourcePrefix(Authentication authentication, AuthorizationTypeDTO authorization, String resourcePrefix) {
        if (!checkAuthentication(authentication)) return false;
        if (checkForRoot(authentication)) return true;
        List<AuthorizationDTO> foundLogbookAuth = getAllAuthorizationForOwnerAndAndAuthTypeAndResourcePrefix(
                authentication.getCredentials().toString(),
                authorization,
                resourcePrefix,
                Optional.empty()
        );
        return !foundLogbookAuth.isEmpty();
    }

    /**
     * Update the authorization type
     */
    abstract public String updateAuthorizationType(String authorizationId, AuthorizationTypeDTO authorizationTypeDTO);

    /**
     * Check and in case create the authorization
     */
    abstract public String ensureAuthorization(AuthorizationDTO authorization);

    /**
     * Create a new authorization in case
     *
     * @param newAuthorizationDTO the new authorization
     */
    abstract public String addNewAuthorization(NewAuthorizationDTO newAuthorizationDTO);

    /**
     * Find all authorization for a resource
     *
     * @param authorizationId the id
     */
    abstract public void deleteAuthorizationById(String authorizationId);

    /**
     * Find all authorization for a resource
     *
     * @param resource the resource
     */
    abstract public List<AuthorizationDTO> findByResourceIs(String resource);

    /**
     * Delete all authorization for a resource prefix
     *
     * @param resourcePrefix the resource prefix
     */
    abstract public void deleteAuthorizationForResourcePrefix(String resourcePrefix);

    /**
     * Delete all authorization for a resource prefix, owner and owner type
     *
     * @param resourcePrefix the resource prefix
     * @param ownerId        the owner id
     * @param ownerType      the owner type
     */
    abstract public void deleteAuthorizationForResourcePrefix(String resourcePrefix, String ownerId, AuthorizationOwnerTypeDTO ownerType);
    /**
     * Delete all authorization for a resource prefix and owner type
     *
     * @param resourcePrefix the resource prefix
     * @param ownerType      the owner type
     */
    abstract public void deleteAuthorizationForResourcePrefix(String resourcePrefix, AuthorizationOwnerTypeDTO ownerType);
    /**
     * Delete all authorization for a resource
     *
     * @param resource the resource
     */
    abstract public void deleteAuthorizationForResource(String resource);

    /**
     * return all authorization for a resource
     *
     * @param owner the owner
     * @param authorizationType the authorization type
     * @param resourcePrefix the resource prefix
     * @param allHigherAuthOnSameResource return only the higher authorization for each resource
     */
    abstract public List<AuthorizationDTO> getAllAuthorizationForOwnerAndAndAuthTypeAndResourcePrefix(String owner, AuthorizationTypeDTO authorizationType, String resourcePrefix, Optional<Boolean> allHigherAuthOnSameResource);

    /**
     * return all authorization for an owner
     *
     * @param owner the owner
     * @param resourcePrefix the resource prefix
     */
    abstract public List<AuthorizationDTO> getAllAuthenticationForOwner(String owner, AuthorizationOwnerTypeDTO ownerType, String resourcePrefix, Optional<Boolean> allHigherAuthOnSameResource);

    /**
     * return all authorization for an owner
     *
     * @param owner the owner
     */
    abstract public List<AuthorizationDTO> getAllAuthenticationForOwner(String owner, AuthorizationOwnerTypeDTO ownerType, Optional<Boolean> allHigherAuthOnSameResource);

    /**
     * Automatically manage root user by configuration
     */
    abstract public void updateRootUser();

    /**
     * Automatically update the root token
     */
    abstract public void updateAutoManagedRootToken();

    /**
     * add a new root authorization for the given email
     * @param email the email
     * @param creator the creator
     */
    abstract public void addRootAuthorization(String email, String creator);

    /**
     * Delete the root authorization
     *
     * @param email the email
     */
    abstract public void removeRootAuthorization(String email);

    /**
     * Ensure that the authentication token is created and exists
     *
     * @param authenticationToken the authentication token
     */
    abstract public String ensureAuthenticationToken(AuthenticationToken authenticationToken);

    /**
     * Add a new authentication token application specific that mean the email is @slac.stanford.edu$
     *
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged                if the token is managed by the application
     */
    abstract public AuthenticationTokenDTO addNewAuthenticationToken(NewAuthenticationTokenDTO newAuthenticationTokenDTO, boolean appManaged);

    /**
     * add a new authentication token application specific that mean the email is @slac.stanford.edu$
     *
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged                if the token is managed by the application
     */
    abstract public AuthenticationTokenDTO addNewApplicationAuthenticationToken(NewAuthenticationTokenDTO newAuthenticationTokenDTO, boolean appManaged);

    /**
     * Return all authentication token by name
     */
    abstract public Optional<AuthenticationTokenDTO> getAuthenticationTokenByName(String name);

    /**
     * Return all authentication token
     */
    abstract public List<AuthenticationTokenDTO> getAllAuthenticationToken();

    abstract public List<AuthenticationTokenDTO> findAllAuthenticationToken(AuthenticationTokenQueryParameterDTO build);

    /**
     * Delete a token by id
     *
     * @param id the id
     */
    abstract public void deleteToken(String id);

    /**
     * Get an authentication token by id
     *
     * @param id the id
     */
    abstract public Optional<AuthenticationTokenDTO> getAuthenticationTokenById(String id);

    /**
     * Check if the token, identified by the email, exists
     *
     * @param email the email
     */
    abstract public boolean existsAuthenticationTokenByEmail(String email);

    /**
     * Return all authentication token that have an email
     *
     * @param email the email
     */
    abstract public Optional<AuthenticationTokenDTO> getAuthenticationTokenByEmail(String email);

    /**
     * Delete and authentication token
     *
     * @param emailPostfix the email postfix
     */
    abstract public void deleteAllAuthenticationTokenWithEmailEndWith(String emailPostfix);

    /**
     * Return all authentication token that have an email that end with the given postfix
     *
     * @param emailPostfix the email postfix
     */
    abstract public List<AuthenticationTokenDTO> getAuthenticationTokenByEmailEndsWith(String emailPostfix);

    /**
     * check if the current authentication can manage the group
     * @param authentication the current authentication
     */
    public abstract boolean canManageGroup(Authentication authentication);

    /**
     * authorize an user to manage groups
     * @param userId the user id
     */
    public abstract void authorizeUserIdToManageGroup(String userId);

    /**
     * check if the current authentication can manage the group
     * @param authorizationGroupManagementDTO the user id
     */
    abstract public void manageAuthorizationOnGroup(AuthorizationGroupManagementDTO authorizationGroupManagementDTO);

    /**
     * check if the current authentication can manage the group
     * @param userId the user id
     */
    public abstract void removeAuthorizationToUserIdToManageGroup(String userId);

    /**
     * Return all authentication token that have an email that end with the given postfix
     *
     * @param userIds the list of user ids
     */
    abstract public List<UserGroupManagementAuthorizationLevel> getGroupManagementAuthorization(List<String> userIds);

    /**
     * Return all authentication token that have an email that end with the given postfix
     *
     * @param newGroupDTO the new group
     */
    abstract public String createLocalGroup(NewLocalGroupDTO newGroupDTO);

    /**
     * Update the local group
     *
     * @param localGroupId the local group id
     * @param updateGroupDTO the information to update
     */
    abstract public void updateLocalGroup(String localGroupId, UpdateLocalGroupDTO updateGroupDTO);

    /**
     * Return all authentication token that have an email that end with the given postfix
     *
     * @param localGroupId the local group id
     */
    abstract public void deleteLocalGroup(String localGroupId);

    /**
     * Return al the local group paging with search on name and description
     *
     * @param build the query parameter
     */
    abstract public List<LocalGroupDTO> findLocalGroup(LocalGroupQueryParameterDTO build);

    /**
     * Return the local group description by a given id
     *
     * @param localGroupId the local group id
     */
    abstract public LocalGroupDTO findLocalGroupById(String localGroupId);
}
