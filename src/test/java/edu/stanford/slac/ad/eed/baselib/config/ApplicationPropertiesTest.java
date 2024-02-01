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
    @Autowired
    AppProperties appProp;
    @Test
    public void authenticationTokenTest(){
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
        assertThat(
                appProp.isAppTokenEmail(
                        "email@%s".formatted(appProp.getAppEmailPostfix())
                )
        ).isEqualTo(
                true
        );
    }
    @Test
    public void authenticationServiceTokenTest(){
        assertThat(
                appProp.isAuthenticationToken(
                        "email@%s".formatted(appProp.getAuthenticationTokenDomain())
                )
        ).isEqualTo(
                true
        );
    }

    @Test
    public void testInternalServiceEmail(){
        assertThat(
                appProp.isServiceInternalTokenEmail(
                        appProp.getInternalServiceTokenEmail()
                )
        ).isEqualTo(
                true
        );
    }
}
