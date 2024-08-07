package edu.stanford.slac.ad.eed.baselib.repository;


import edu.stanford.slac.ad.eed.baselib.model.Person;
import org.springframework.data.ldap.repository.LdapRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends LdapRepository<Person>, PersonRepositoryCustom{
    Optional<Person> findByMail(String mail);
    Optional<Person> findByUid(String uid);
    List<Person> findByGecosContainsIgnoreCaseOrderByCommonNameAsc(String commonNamePrefix);
}