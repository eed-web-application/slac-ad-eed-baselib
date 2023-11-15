package edu.stanford.slac.ad.eed.baselib.repository;



import edu.stanford.slac.ad.eed.baselib.model.AuthenticationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuthenticationTokenRepository {
    List<AuthenticationToken> findAll();
    //return all token managed by the application at startup time
    List<AuthenticationToken> findAllByApplicationManagedIsTrue();

    void deleteAllByApplicationManagedIsTrue();

    Optional<AuthenticationToken> findByName(String name);

    boolean existsByName(String name);

    Optional<AuthenticationToken> findByEmailIs(String email);

    Optional<AuthenticationToken> findByNameIsAndEmailEndsWith(String name, String emailPostfix);

    List<AuthenticationToken> findAllByEmailEndsWith(String emailPostfix);

    boolean existsByEmail(String email);

    void deleteAllByEmailEndsWith(String emailPostfix);
}
