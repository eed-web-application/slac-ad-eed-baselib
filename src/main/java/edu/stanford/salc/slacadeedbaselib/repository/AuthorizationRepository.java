package edu.stanford.salc.slacadeedbaselib.repository;



import edu.stanford.salc.slacadeedbaselib.model.AuthenticationToken;
import edu.stanford.salc.slacadeedbaselib.model.Authorization;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for the management of the authorization
 */
public interface AuthorizationRepository extends CrudRepository<Authorization, String> {
    Optional<Authorization> findByOwnerIsAndResourceIsAndAuthorizationTypeIs(String owner, String resource, Integer authorizationType);

    Optional<Authorization> findByOwnerIsAndResourceIsAndAuthorizationTypeIsGreaterThanEqual(String owner, String resource, Integer authorizationType);
    List<Authorization> findByResourceIs(String resource);
    List<Authorization> findByResourceIsAndAuthorizationTypeIsGreaterThanEqual(String resource, Integer authorizationType);
    //@Query("{ 'owner' : '?0', $or: [{'authorizationType' : { '$gte' : '?1'}, 'resource' : { $regex : \"?2\"}}, {'authorizationType' : 2, resource:'*'}]}")
    List<Authorization> findByOwnerAndOwnerTypeAndAuthorizationTypeIsGreaterThanEqualAndResourceStartingWith(String owner, edu.stanford.salc.slacadeedbaselib.model.Authorization.OType ownerType, Integer authorizationType, String resource);
    void deleteByOwnerIsAndResourceIsAndAuthorizationTypeIs(String owner, String resource, Integer authorizationType);
    void deleteAllByResourceStartingWith(String resourcePrefix);
    void deleteAllByResourceIs(String resource);
    void deleteAllByOwnerIs(String owner);
}
