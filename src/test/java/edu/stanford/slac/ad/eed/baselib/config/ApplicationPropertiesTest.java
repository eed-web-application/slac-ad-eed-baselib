package edu.stanford.slac.ad.eed.baselib.config;

import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@AutoConfigureMockMvc
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
public class ApplicationPropertiesTest {
    @MockBean
    AuthService authService;
    @Test
    public void authenticationTokenTest(){
        AppProperties appProp = new AppProperties();
        appProp.setAppTokenPrefix("app-a");

        assertThat(
                appProp.isAuthenticationToken(
                        "email@%s".formatted(appProp.getAuthenticationTokenDomain())
                )
        ).isEqualTo(
                true
        );
    }
    @Test
    public void applicationTokenTest(){
        AppProperties appProp = new AppProperties();
        appProp.setAppTokenPrefix("app-a");

        assertThat(
                appProp.isAppTokenEmail(
                        "email@app-a.%s".formatted(appProp.getAuthenticationTokenDomain())
                )
        ).isEqualTo(
                true
        );
    }
    @Test
    public void authenticationServiceTokenTest(){
        AppProperties appProp = new AppProperties();
        appProp.setAppName("app-a");

        assertThat(
                appProp.isAuthenticationToken(
                        "email@%s".formatted(appProp.getAuthenticationTokenDomain())
                )
        ).isEqualTo(
                true
        );
    }
}
