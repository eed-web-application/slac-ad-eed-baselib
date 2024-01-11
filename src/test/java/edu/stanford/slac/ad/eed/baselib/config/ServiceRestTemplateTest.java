package edu.stanford.slac.ad.eed.baselib.config;

import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@AutoConfigureMockMvc
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
public class ServiceRestTemplateTest {
    @Autowired
    AppProperties appProperties;
    @Autowired
    RestTemplate serviceRestTemplate;
    @MockBean
    AuthService authService;
    @Test
    public void testServiceRestTemplate(){
        var mockServer = MockRestServiceServer.createServer(serviceRestTemplate);
        mockServer.expect(requestTo("http://example.com/api"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(
                        request -> {
                            HttpHeaders headers = request.getHeaders();
                            assertThat(headers.containsKey(appProperties.getUserHeaderName())).isNotNull();
                        }
                )
                .andRespond(withSuccess("response body", MediaType.TEXT_PLAIN));

        // Perform the REST call
        serviceRestTemplate.postForObject("http://example.com/api", new HttpEntity<>("request body"), String.class);

        // Verify all expectations met
        mockServer.verify();
    }
}
