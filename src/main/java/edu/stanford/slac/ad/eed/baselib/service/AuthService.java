package edu.stanford.slac.ad.eed.baselib.service;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.*;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.model.AuthenticationToken;
import edu.stanford.slac.ad.eed.baselib.model.Authorization;
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
     * Check and in case create the authorization
     */
    abstract  public String ensureAuthorization(Authorization authorization);

    /**
     * Create a new authorization in case
     * @param newAuthorizationDTO
     */
    abstract public String addNewAuthorization(NewAuthorizationDTO newAuthorizationDTO);
    abstract public void deleteAuthorizationById(String authorizationId);
    abstract public List<AuthorizationDTO> findByResourceIs(String resource);
    abstract  public void deleteAuthorizationForResourcePrefix(String resourcePrefix);
    abstract  public void deleteAuthorizationForResource(String resource);
    abstract  public List<AuthorizationDTO> getAllAuthorizationForOwnerAndAndAuthTypeAndResourcePrefix(String owner, AuthorizationTypeDTO authorizationType, String resourcePrefix, Optional<Boolean> allHigherAuthOnSameResource);
    abstract  public void updateRootUser();
    abstract  public void updateAutoManagedRootToken();
    abstract  public void addRootAuthorization(String email, String creator);
    abstract  public void removeRootAuthorization(String email);
    abstract  public String ensureAuthenticationToken(AuthenticationToken authenticationToken);
    abstract  public AuthenticationTokenDTO addNewAuthenticationToken(NewAuthenticationTokenDTO newAuthenticationTokenDTO, boolean appManaged);
    abstract  public Optional<AuthenticationTokenDTO> getAuthenticationTokenByName(String name);
    abstract  public List<AuthenticationTokenDTO> getAllAuthenticationToken();
    abstract  public void deleteToken(String id);
    abstract  public Optional<AuthenticationTokenDTO> getAuthenticationTokenById(String id);
    abstract  public boolean existsAuthenticationTokenByEmail(String email);
    abstract  public Optional<AuthenticationTokenDTO> getAuthenticationTokenByEmail(String email);
    abstract  public void deleteAllAuthenticationTokenWithEmailEndWith(String emailPostfix);
    abstract  public List<AuthenticationTokenDTO> getAuthenticationTokenByEmailEndsWith(String id);
}
