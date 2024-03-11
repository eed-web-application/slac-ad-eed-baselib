package edu.stanford.slac.ad.eed.baselib.mapper;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthenticationTokenDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.NewAuthenticationTokenDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.mapper.AuthMapper;
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
public class MapperTest {
    @Autowired
    AuthMapper authMapper;
    @MockBean
    AuthService authService;
    @Test
    public void testCustomAppAuthenticationTokenFromNewDTO() {
        var authToken = authMapper.toModelApplicationToken(
                NewAuthenticationTokenDTO
                        .builder()
                        .name("token-name")
                        .build(),
                "custom-app-prefix"
        );

        assertThat(authToken.getEmail()).isEqualTo("token-name@custom-app-prefix.test-app.slac.app$");
    }

    @Test
    public void testCustomAppAuthenticationTokenFromDTO() {
        var authToken = authMapper.toModelAuthenticationToken(
                AuthenticationTokenDTO
                        .builder()
                        .name("token-name")
                        .build(),
                "custom-app-prefix"
        );

        assertThat(authToken.getEmail()).isEqualTo("token-name@custom-app-prefix.test-app.slac.app$");
    }
}
