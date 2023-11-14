package edu.stanford.salc.slacadeedbaselib.repository;


import edu.stanford.salc.slacadeedbaselib.model.Person;
import org.springframework.data.ldap.repository.LdapRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends LdapRepository<Person> {
    Optional<Person> findByMail(String mail);
    List<Person> findByGecosContainsIgnoreCaseOrderByCommonNameAsc(String commonNamePrefix);
}