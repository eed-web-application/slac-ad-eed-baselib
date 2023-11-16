package edu.stanford.slac.ad.eed.baselib.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

@Configuration
@EnableLdapRepositories(basePackages = "edu.stanford.slac.ad.eed.baselib.repository")
public class InitLDAP {
}
