package edu.stanford.slac.ad.eed.baselib.service;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.*;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.model.AuthenticationToken;
import edu.stanford.slac.ad.eed.baselib.model.Authorization;
import edu.stanford.slac.ad.eed.baselib.model.AuthorizationOwnerType;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public abstract class AuthService{
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
    @Cacheable(value = "user-root-authorization", key = "{#authentication.credentials}", unless = "#authentication == null")
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
    @Cacheable(value = "user-authorization", key = "{#authentication.credentials, #authorization, #resourcePrefix}", unless = "#authentication == null")
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
    abstract  public String updateAuthorizationType(String authorizationId, AuthorizationTypeDTO authorizationTypeDTO);

    /**
     * Check and in case create the authorization
     */
    abstract  public String ensureAuthorization(AuthorizationDTO authorization);

    /**
     * Create a new authorization in case
     * @param newAuthorizationDTO the new authorization
     */
    abstract public String addNewAuthorization(NewAuthorizationDTO newAuthorizationDTO);
    /**
     * Find all authorization for a resource
     * @param resource the resource
     */
    abstract public void deleteAuthorizationById(String authorizationId);
    /**
     * Find all authorization for a resource
     * @param resource the resource
     */
    abstract public List<AuthorizationDTO> findByResourceIs(String resource);
    /**
     * Delete all authorization for a resource prefix
     * @param resourcePrefix the resource prefix
     */
    abstract  public void deleteAuthorizationForResourcePrefix(String resourcePrefix);
    /**
     * Delete all authorization for a resource prefix and owner
     * @param resourcePrefix the resource prefix
     * @param ownerId the owner id
     * @param ownerType the owner type
     */
    abstract  public void deleteAuthorizationForResourcePrefix(String resourcePrefix, String ownerId, AuthorizationOwnerType ownerType);
    /**
     * Delete all authorization for a resource
     * @param resource the resource
     */
    abstract  public void deleteAuthorizationForResource(String resource);
    /**
     * Delete all authorization for a resource and owner
     * @param resource the resource
     * @param ownerId the owner id
     * @param ownerType the owner type
     */
    abstract  public List<AuthorizationDTO> getAllAuthorizationForOwnerAndAndAuthTypeAndResourcePrefix(String owner, AuthorizationTypeDTO authorizationType, String resourcePrefix, Optional<Boolean> allHigherAuthOnSameResource);
    /**
     * Automatically manage root user by configuration
     * @param resource the resource
     */
    abstract  public void updateRootUser();
    /**
     * Automatically update the root token
     * @param resource the resource
     */
    abstract  public void updateAutoManagedRootToken();
    /**
     * Automatically update the root token
     * @param resource the resource
     */
    abstract  public void addRootAuthorization(String email, String creator);
    /**
     * Automatically update the root token
     * @param resource the resource
     */
    abstract  public void removeRootAuthorization(String email);
    /**
     * Automatically update the root token
     * @param resource the resource
     */
    abstract  public String ensureAuthenticationToken(AuthenticationToken authenticationToken);
    /**
     * Add a new authentication token application specific that mean the email is @slac.stanford.edu$
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged if the token is managed by the application
     */
    abstract  public AuthenticationTokenDTO addNewAuthenticationToken(NewAuthenticationTokenDTO newAuthenticationTokenDTO, boolean appManaged);
    /**
     * Add a new authentication token application specific that mean the email is @app-name.slac.stanford.edu$
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged if the token is managed by the application
     */
    abstract  public AuthenticationTokenDTO addNewApplicationAuthenticationToken(NewAuthenticationTokenDTO newAuthenticationTokenDTO, boolean appManaged);
    /**
     * Add a new authentication token application specific that mean the email is @app-name.slac.stanford.edu$
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged if the token is managed by the application
     */
    abstract  public Optional<AuthenticationTokenDTO> getAuthenticationTokenByName(String name);
    /**
     * Add a new authentication token application specific that mean the email is @app-name.slac.stanford.edu$
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged if the token is managed by the application
     */
    abstract  public List<AuthenticationTokenDTO> getAllAuthenticationToken();
    /**
     * Add a new authentication token application specific that mean the email is @app-name.slac.stanford.edu$
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged if the token is managed by the application
     */
    abstract  public void deleteToken(String id);
    /**
     * Add a new authentication token application specific that mean the email is @app-name.slac.stanford.edu$
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged if the token is managed by the application
     */
    abstract  public Optional<AuthenticationTokenDTO> getAuthenticationTokenById(String id);
    /**
     * Add a new authentication token application specific that mean the email is @app-name.slac.stanford.edu$
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged if the token is managed by the application
     */
    abstract  public boolean existsAuthenticationTokenByEmail(String email);
    /**
     * Add a new authentication token application specific that mean the email is @app-name.slac.stanford.edu$
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged if the token is managed by the application
     */
    abstract  public Optional<AuthenticationTokenDTO> getAuthenticationTokenByEmail(String email);
    /**
     * Add a new authentication token application specific that mean the email is @app-name.slac.stanford.edu$
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged if the token is managed by the application
     */
    abstract  public void deleteAllAuthenticationTokenWithEmailEndWith(String emailPostfix);
    /**
     * Add a new authentication token application specific that mean the email is @app-name.slac.stanford.edu$
     * @param newAuthenticationTokenDTO the new authentication token
     * @param appManaged if the token is managed by the application
     */
    abstract  public List<AuthenticationTokenDTO> getAuthenticationTokenByEmailEndsWith(String id);
}
