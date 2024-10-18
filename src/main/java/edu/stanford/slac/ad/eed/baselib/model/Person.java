package edu.stanford.slac.ad.eed.baselib.model;

import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Entry(
        base = "dc=win,dc=slac,dc=stanford,dc=edu",
        objectClasses = { "person", "user", "organizationalPerson" }
)
@Data
public final class Person {

    @Id
    private Name id;

    @Attribute(name = "uid")
    private String uid;

    @Attribute(name = "mail")
    private String mail;

    @Attribute(name = "name")
    private String gecos;

    @Attribute(name = "givenName")
    private String givenName;

    @Attribute(name = "cn")
    private String commonName;

    @Attribute(name = "sn")
    private String lastName;
}