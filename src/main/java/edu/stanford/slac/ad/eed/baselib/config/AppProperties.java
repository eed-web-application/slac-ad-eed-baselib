package edu.stanford.slac.ad.eed.baselib.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.NewAuthenticationTokenDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Getter
@Setter
@ConfigurationProperties(prefix = "edu.stanford.slac")
public class AppProperties {
    /**
     * The application token are generated using a fake email for identify the user
     * for example: token@slac.app$ is a global token. A token for only a specific applcaition
     * will have the form: token-a@{appTokenPrefix}.slac.app$
     */
    private String appTokenPrefix;
    private String appTokenJwtKey;
    private String dbAdminUri;
    private String userHeaderName;
    private String oauthServerDiscover;
    private List<String> rootUserList = new ArrayList<>();
    private List<NewAuthenticationTokenDTO> rootAuthenticationTokenList = new ArrayList<>();
    private String rootAuthenticationTokenListJson = "{}";
    // all email that belong to this domain belongs to application toke authorization
    private final String applicationTokenDomain = "slac.app$";
    private final String applicationEmailRegex = ".*@%s\\.slac\\.app\\$";

    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rootAuthenticationTokenList = objectMapper.readValue(rootAuthenticationTokenListJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    public String getApplication
    /**
     * Return the application token domain regex on the form
     * of ".*@{appTokenPrefix}\.slac\.app\$"
     * @return the application domain
     */
    public String getAppTokenRegex() {
        return applicationEmailRegex.formatted(appTokenPrefix);
    }
}
