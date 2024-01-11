package edu.stanford.slac.ad.eed.baselib.config;

import edu.stanford.slac.ad.eed.baselib.auth.JWTHelper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
@Component

public class ServiceHeaderRequestInterceptor implements ClientHttpRequestInterceptor {
    private final JWTHelper jwtHelper;
    private final AppProperties appProperties;
    private String serviceJwtToken = null;

    public ServiceHeaderRequestInterceptor(JWTHelper jwtHelper, AppProperties appProperties) {
        this.jwtHelper = jwtHelper;
        this.appProperties = appProperties;
    }

    @Override
    @NonNull
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        if(serviceJwtToken==null) {
            serviceJwtToken = jwtHelper.generateServiceToken();
        }
        request.getHeaders().set(appProperties.getUserHeaderName(), serviceJwtToken);
        return execution.execute(request, body);
    }
}
