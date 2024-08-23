package edu.stanford.slac.ad.eed.baselib.repository;

import edu.stanford.slac.ad.eed.baselib.model.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapName;

@Log4j2
public class PersonAttributesMapper implements AttributesMapper<Person> {

    @Override
    public Person mapFromAttributes(Attributes attrs) throws NamingException {
        Person person = new Person();
        // print in line all the attributes
        log.info("Attributes: {}", attrs);
        if (attrs.get("uid") != null) {
            person.setUid((String) attrs.get("uid").get());
        }
        if (attrs.get("mail") != null) {
            person.setMail((String) attrs.get("mail").get());
        }
        if (attrs.get("name") != null) {
            person.setGecos((String) attrs.get("name").get());
        }
        if (attrs.get("cn") != null) {
            person.setCommonName((String) attrs.get("cn").get());
        }
        if (attrs.get("sn") != null) {
            person.setLastName((String) attrs.get("sn").get());
        }
        if (attrs.get("givenName") != null) {
            person.setGivenName((String) attrs.get("givenName").get());
        }
        // Handle the ID (Distinguished Name)
        // Assuming the id field is of type Name which represents the DN
        if (attrs.get("distinguishedName") != null) {
            person.setId(new LdapName((String) attrs.get("distinguishedName").get()));
        }
        return person;
    }
}