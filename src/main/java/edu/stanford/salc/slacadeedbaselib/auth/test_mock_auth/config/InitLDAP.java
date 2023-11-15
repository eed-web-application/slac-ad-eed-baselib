package edu.stanford.salc.slacadeedbaselib.auth.test_mock_auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

@Configuration
@EnableLdapRepositories(basePackages = "edu.stanford.slac.ldap_repository")
public class InitLDAP {
}
