package edu.stanford.slac.ad.eed.baselib.service;


import edu.stanford.slac.ad.eed.baselib.api.v1.dto.*;
import edu.stanford.slac.ad.eed.baselib.api.v1.mapper.AuthMapper;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.model.AuthenticationToken;
import edu.stanford.slac.ad.eed.baselib.model.Person;
import edu.stanford.slac.ad.eed.baselib.repository.GroupRepository;
import edu.stanford.slac.ad.eed.baselib.repository.PersonRepository;
import edu.stanford.slac.ad.eed.baselib.auth.JWTHelper;
import edu.stanford.slac.ad.eed.baselib.exception.*;
import edu.stanford.slac.ad.eed.baselib.model.Group;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static edu.stanford.slac.ad.eed.baselib.exception.Utility.assertion;
import static edu.stanford.slac.ad.eed.baselib.exception.Utility.wrapCatch;
import static edu.stanford.slac.ad.eed.baselib.utility.StringUtilities.tokenNameNormalization;


@Service
@Log4j2
@AllArgsConstructor
public class AuthService {
    private final JWTHelper jwtHelper;
    private final AuthMapper authMapper;
    private final AppProperties appProperties;
    private final PersonRepository personRepository;
    private final GroupRepository groupRepository;
    private final AuthServiceAbstractInterface authServiceAbstractInterface;
    public PersonDTO findPerson(Authentication authentication) {
        return personRepository.findByMail(
                authentication.getCredentials().toString()
        ).map(
                authMapper::fromModel
        ).orElseThrow(
                () -> UserNotFound.userNotFound()
                        .errorCode(-2)
                        .errorDomain("AuthService::findPerson")
                        .build()
        );
    }

    public List<PersonDTO> findPersons(String searchString) throws UsernameNotFoundException {
        List<Person> foundPerson = personRepository.findByGecosContainsIgnoreCaseOrderByCommonNameAsc(
                searchString
        );
        return foundPerson.stream().map(
                authMapper::fromModel
        ).toList();
    }

    /**
     * Find the group by filtering on name
     *
     * @param searchString search string for the group name
     * @return the list of found groups
     */
    public List<GroupDTO> findGroup(String searchString) throws UsernameNotFoundException {
        List<Group> foundPerson = groupRepository.findByCommonNameContainsIgnoreCaseOrderByCommonNameAsc(searchString);
        return foundPerson.stream().map(
                authMapper::fromModel
        ).toList();
    }

    /**
     * find all group for the user
     *
     * @param userId is the user id
     * @return the list of the groups where the user belong
     */
    private List<GroupDTO> findGroupByUserId(String userId) {
        List<Group> findGroups = groupRepository.findByMemberUidContainingIgnoreCase(userId);
        return findGroups.stream().map(
                authMapper::fromModel
        ).toList();
    }

    /**
     * Check if the current authentication is authenticated
     *
     * @param authentication the current authentication
     */
    public boolean checkAuthentication(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

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
     * Delete an authorizations for a resource with a specific prefix
     *
     * @param resourcePrefix the prefix of the resource
     */
    public void deleteAuthorizationForResourcePrefix(String resourcePrefix) {
        authServiceAbstractInterface.deleteAuthorizationForResourcePrefix(resourcePrefix);
    }

    /**
     * Delete an authorizations for a resource with a specific path
     *
     * @param resource the path of the resource
     */
    public void deleteAuthorizationForResource(String resource) {
        authServiceAbstractInterface.deleteAuthorizationForResource(resource);
    }

    @Cacheable(value = "user-authorization", key = "{#owner, #authorizationType, #resourcePrefix}")
    public List<AuthorizationDTO> getAllAuthorizationForOwnerAndAndAuthTypeAndResourcePrefix(
            String owner,
            AuthorizationTypeDTO authorizationType,
            String resourcePrefix) {
        return getAllAuthorizationForOwnerAndAndAuthTypeAndResourcePrefix(
                owner,
                authorizationType,
                resourcePrefix,
                Optional.empty()
        );
    }

    /**
     * Return all the authorizations for an owner that match with the prefix
     * and the authorizations type, if the owner is of type 'User' will be checked for all the
     * entries all along with those that belongs to all the user groups.
     *
     * @param owner                       si the owner target of the result authorizations
     * @param authorizationType           filter on the @Authorization.Type
     * @param resourcePrefix              is the prefix of the authorized resource
     * @param allHigherAuthOnSameResource return only the higher authorization for each resource
     * @return the list of found resource
     */
    @Cacheable(value = "user-authorization", key = "{#owner, #authorizationType, #resourcePrefix, #allHigherAuthOnSameResource}")
    public List<AuthorizationDTO> getAllAuthorizationForOwnerAndAndAuthTypeAndResourcePrefix(
            String owner,
            AuthorizationTypeDTO authorizationType,
            String resourcePrefix,
            Optional<Boolean> allHigherAuthOnSameResource
    ) {
        return authServiceAbstractInterface.getAllAuthorizationForOwnerAndAndAuthTypeAndResourcePrefix(
                owner,
                authorizationType,
                resourcePrefix,
                allHigherAuthOnSameResource
        );
    }

    /**
     * Update all configured root user
     */
    public void updateRootUser() {
        authServiceAbstractInterface.updateRootUser();
    }

    /**
     *
     */
    public void updateAutoManagedRootToken() {
        authServiceAbstractInterface.updateAutoManagedRootToken();
    }

    /**
     * Add user identified by email as root
     *
     * @param email the user email
     */
    public void addRootAuthorization(String email, String creator) {
        authServiceAbstractInterface.addRootAuthorization(email, creator);
    }

    /**
     * Remove user identified by email as root user
     *
     * @param email that identify the user
     */
    public void removeRootAuthorization(String email) {
        authServiceAbstractInterface.removeRootAuthorization(email);
    }

    /**
     * Return all root authorization
     *
     * @return all the root authorization
     */
    public List<AuthorizationDTO> findAllRoot() {
        return authServiceAbstractInterface.findAllRootUser();
    }

    /**
     * Ensure token
     *
     * @param authenticationToken token to ensure
     */
    public String ensureAuthenticationToken(AuthenticationToken authenticationToken) {
        return authServiceAbstractInterface.ensureAuthenticationToken(authenticationToken);
    }
    public AuthenticationTokenDTO addNewAuthenticationToken(NewAuthenticationTokenDTO newAuthenticationTokenDTO) {
        return addNewAuthenticationToken(newAuthenticationTokenDTO, false);
    }
    /**
     * Add a new authentication token
     *
     * @param newAuthenticationTokenDTO is the new token information
     */
    public AuthenticationTokenDTO addNewAuthenticationToken(NewAuthenticationTokenDTO newAuthenticationTokenDTO, boolean appManaged) {
        return authServiceAbstractInterface.addNewAuthenticationToken(newAuthenticationTokenDTO, appManaged);
    }

    private AuthenticationToken getAuthenticationToken(NewAuthenticationTokenDTO newAuthenticationTokenDTO, boolean appManaged) {
        AuthenticationToken authTok = authMapper.toModelToken(
                newAuthenticationTokenDTO.toBuilder()
                        .name(
                                tokenNameNormalization(
                                        newAuthenticationTokenDTO.name()
                                )
                        )
                        .build()
        );
        return authTok.toBuilder()
                .applicationManaged(appManaged)
                .token(
                        jwtHelper.generateAuthenticationToken(
                                authTok
                        )
                )
                .build();
    }

    /**
     * Return an application token by name
     *
     * @param name the name of the token to return
     * @return the found authentication token
     */
    public Optional<AuthenticationTokenDTO> getAuthenticationTokenByName(String name) {
        return authServiceAbstractInterface.getAuthenticationTokenByName(name);
    }

    /**
     * return al the global authentication tokens
     *
     * @return the list of all authentication tokens
     */
    public List<AuthenticationTokenDTO> getAllAuthenticationToken() {
        return authServiceAbstractInterface.getAllAuthenticationToken();
    }

    /**
     * Delete a token by name along with all his authorization records
     *
     * @param id the token id
     */
    @Transactional
    public void deleteToken(String id) {
        authServiceAbstractInterface.deleteToken(id);
    }

    /**
     * Return an application token by name
     *
     * @param id the unique id of the token to return
     * @return the found authentication token
     */
    public Optional<AuthenticationTokenDTO> getAuthenticationTokenById(String id) {
        return authServiceAbstractInterface.getAuthenticationTokenById(id);
    }

    /**
     * @param email
     * @return
     */
    public boolean existsAuthenticationTokenByEmail(String email) {
        return authServiceAbstractInterface.existsAuthenticationTokenByEmail(email);
    }

    /**
     * Return the authentication token by email
     * @param email the email of the authentication token to return
     * @return the authentication token found
     */
    public Optional<AuthenticationTokenDTO> getAuthenticationTokenByEmail(String email) {
        return authServiceAbstractInterface.getAuthenticationTokenByEmail(email);
    }

    /**
     * delete all the authorization where the email ends with the postfix
     *
     * @param emailPostfix the terminal string of the email
     */
    public void deleteAllAuthenticationTokenWithEmailEndWith(String emailPostfix) {
        authServiceAbstractInterface.deleteAllAuthenticationTokenWithEmailEndWith(emailPostfix);
    }
}
