package edu.stanford.slac.ad.eed.baselib.repository;

import edu.stanford.slac.ad.eed.baselib.model.Person;
import edu.stanford.slac.ad.eed.baselib.model.PersonQueryParameter;
import lombok.AllArgsConstructor;
import org.springframework.ldap.control.SortControlDirContextProcessor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.*;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository
@AllArgsConstructor
public class PersonRepositoryImpl implements PersonRepositoryCustom {
    private LdapTemplate ldapTemplate;

    @Override
    public List<Person> findAll(PersonQueryParameter personQueryParameter) {
        // Build query based on lastCn for cursor-based pagination
        LdapQuery query = null;
        List<Person> result = new LinkedList<>();
//        result.addAll(getContextResult(personQueryParameter));
        result.addAll(getLimitResult(personQueryParameter));
        return result;
    }

    /**
     * Get the next page of results based on the last user email
     * @param personQueryParameter
     * @return
     */
    private List<Person> getContextResult(PersonQueryParameter personQueryParameter) {
        List<Person> result;
        SortControlDirContextProcessor sortControl = new SortControlDirContextProcessor("mail");
        if (personQueryParameter.getAnchor() != null && !personQueryParameter.getAnchor().isEmpty() &&
                personQueryParameter.getContext() != null && personQueryParameter.getContext()>0) {
            var conditionCriteria = LdapQueryBuilder.query()
                    .base("ou=people")
                    .countLimit(personQueryParameter.getContext())
                    .where("objectclass").is("person");

            if(personQueryParameter.getSearchFilter() != null && !personQueryParameter.getSearchFilter().isEmpty()) {
                conditionCriteria = conditionCriteria.or(
                        query()
                                .where("cn").like(personQueryParameter.getSearchFilter())
                                .or(
                                        query().where("sn").like(personQueryParameter.getSearchFilter())
                                )
                );
            }
            conditionCriteria = conditionCriteria.and("mail").lte(personQueryParameter.getAnchor());

            result = ldapTemplate.search(conditionCriteria, new PersonAttributesMapper());
        } else {
            result = Collections.emptyList();
        }
        return result;
    }

    /**
     * Get the next page of results based on the limit size
     * @param personQueryParameter
     * @return
     */
    private List<Person> getLimitResult(PersonQueryParameter personQueryParameter) {
        List<Person> result;

        // Create the search filter
        Filter searchFilter = null;
        if (personQueryParameter.getSearchFilter() != null && !personQueryParameter.getSearchFilter().isEmpty()) {
            OrFilter orFilter = new OrFilter();
            orFilter.or(new LikeFilter("gecos", "*" + personQueryParameter.getSearchFilter() + "*"));
            searchFilter = orFilter;
        }

        // Handle anchor
        Filter anchorFilter = null;
        if (personQueryParameter.getAnchor() != null && !personQueryParameter.getAnchor().isEmpty()) {
            anchorFilter = new GreaterThanOrEqualsFilter("uid", personQueryParameter.getAnchor().toLowerCase() );
        }

        // Combine filters
        AndFilter finalFilter = new AndFilter();
        finalFilter.and(new LikeFilter("objectclass", "person"));
        if (searchFilter != null) {
            finalFilter.and(searchFilter);
        }
        if (anchorFilter != null) {
            finalFilter.and(anchorFilter);
        }
        var baseCriteria = LdapQueryBuilder.query()
                .base("ou=people")
                // with anchor LDAP returns always the anchor, so limit is increased by +1
                .countLimit(personQueryParameter.getAnchor() != null ? personQueryParameter.getLimit() + 1 : personQueryParameter.getLimit())
                .filter(finalFilter);
        result = ldapTemplate.search(baseCriteria, new PersonAttributesMapper());

        if (personQueryParameter.getAnchor() != null && !personQueryParameter.getAnchor().isEmpty() && result.size() > 0) {
            result.remove(0); // Remove the first element as it is the last element from the previous page
        }
        return result;
    }
}
