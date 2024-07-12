package edu.stanford.slac.ad.eed.baselib.repository;
import edu.stanford.slac.ad.eed.baselib.model.Person;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapName;

public class PersonAttributesMapper implements AttributesMapper<Person> {

    @Override
    public Person mapFromAttributes(Attributes attrs) throws NamingException {
        Person person = new Person();

        if (attrs.get("uid") != null) {
            person.setUid((String) attrs.get("uid").get());
        }
        if (attrs.get("mail") != null) {
            person.setMail((String) attrs.get("mail").get());
        }
        if (attrs.get("gecos") != null) {
            person.setGecos((String) attrs.get("gecos").get());
        }
        if (attrs.get("cn") != null) {
            person.setCommonName((String) attrs.get("cn").get());
        }
        if (attrs.get("sn") != null) {
            person.setSurname((String) attrs.get("sn").get());
        }

        // Handle the ID (Distinguished Name)
        // Assuming the id field is of type Name which represents the DN
        if (attrs.get("dn") != null) {
            person.setId(new LdapName((String) attrs.get("dn").get()));
        }

        return person;
    }
}