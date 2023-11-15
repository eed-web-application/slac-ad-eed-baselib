package edu.stanford.slac.ad.eed.baselib.service;


import edu.stanford.slac.ad.eed.baselib.api.v1.dto.*;
import edu.stanford.slac.ad.eed.baselib.api.v1.mapper.AuthMapper;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.model.Person;
import edu.stanford.slac.ad.eed.baselib.repository.GroupRepository;
import edu.stanford.slac.ad.eed.baselib.repository.PersonRepository;
import edu.stanford.slac.ad.eed.baselib.auth.JWTHelper;
import edu.stanford.slac.ad.eed.baselib.exception.*;
import edu.stanford.slac.ad.eed.baselib.model.Group;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static edu.stanford.slac.ad.eed.baselib.exception.Utility.assertion;
import static edu.stanford.slac.ad.eed.baselib.exception.Utility.wrapCatch;


@Service
@Log4j2
@AllArgsConstructor
public class PeopleGroupService {
    private final AuthMapper authMapper;
    private final PersonRepository personRepository;
    private final GroupRepository groupRepository;
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
}
