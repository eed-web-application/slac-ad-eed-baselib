package edu.stanford.slac.ad.eed.baselib.repository;

import edu.stanford.slac.ad.eed.baselib.model.Group;
import org.springframework.data.ldap.repository.LdapRepository;

import java.util.List;

public interface GroupRepository extends LdapRepository<Group> {
    List<Group> findByCommonNameContainsIgnoreCaseOrderByCommonNameAsc(String commonNamePrefix);

    List<Group> findByMemberUidContainingIgnoreCase(String memberUid);
}