package edu.stanford.slac.ad.eed.baselib.config;

import edu.stanford.slac.ad.eed.baselib.auth.JWTHelper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Configuration for the service rest client
 * this kind of rest client is used to this service to communicate with other services
 * of the same category
 */
@Configuration
@AllArgsConstructor
public class ServiceRestClient {
    private final AppProperties appProperties;
    private final JWTHelper jwtHelper;
    private ServiceHeaderRequestInterceptor headerInterceptor;
    @Bean
    public RestTemplate serviceRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // Add the interceptor to the RestTemplate
        restTemplate.setInterceptors(Collections.singletonList(headerInterceptor));
        return restTemplate;
    }
}
