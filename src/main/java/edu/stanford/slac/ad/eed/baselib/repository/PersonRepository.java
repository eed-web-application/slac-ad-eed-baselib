package edu.stanford.slac.ad.eed.baselib.repository;


import edu.stanford.slac.ad.eed.baselib.model.Person;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.data.ldap.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends LdapRepository<Person>, PersonRepositoryCustom{
    @Query("(mail={0})")
    Optional<Person> findByMail(String mail);
    @Query("(uid={0})")
    Optional<Person> findByUid(String uid);
    @Query("(name=*{0}*)")
    List<Person> findByGecosContainsIgnoreCaseOrderByCommonNameAsc(String commonNamePrefix);
}