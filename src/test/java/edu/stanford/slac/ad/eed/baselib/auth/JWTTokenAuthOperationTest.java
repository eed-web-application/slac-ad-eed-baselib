package edu.stanford.slac.ad.eed.baselib.auth;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationTypeDTO;
import edu.stanford.slac.ad.eed.baselib.auth.jwt.SLACAuthenticationJWTToken;
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

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@AutoConfigureMockMvc
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
public class JWTTokenAuthOperationTest {
    @Autowired
    JWTHelper jwtHelper;
    @MockBean
    AuthService authService;
    @Test
    public void testJWTTokenRoot(){
        SLACAuthenticationJWTToken rootServiceToken = new SLACAuthenticationJWTToken(
                null,
                        List.of(
                                AuthorizationDTO
                                        .builder()
                                        .authorizationType(AuthorizationTypeDTO.Admin)
                                        .resource("*")
                                        .build()
                        )
                );
        assertThat(rootServiceToken.isRoot()).isTrue();

        SLACAuthenticationJWTToken nonRootServiceToken = SLACAuthenticationJWTToken
                .authenticated()
                .authorities(
                        List.of(
                                AuthorizationDTO
                                        .builder()
                                        .authorizationType(AuthorizationTypeDTO.Read)
                                        .resource("/r1/1")
                                        .build()
                        )
                )
                .build();
        assertThat(nonRootServiceToken.isRoot()).isFalse();
    }

    @Test
    public void testSearchingAuthTypeForResource(){
        SLACAuthenticationJWTToken rootServiceToken = new SLACAuthenticationJWTToken(
                null,
                List.of(
                        AuthorizationDTO
                                .builder()
                                .authorizationType(AuthorizationTypeDTO.Admin)
                                .resource("/r/1")
                                .build(),
                        AuthorizationDTO
                                .builder()
                                .authorizationType(AuthorizationTypeDTO.Write)
                                .resource("/r/2")
                                .build(),
                        // simulate additional auth form group on same /r/2 resource
                        AuthorizationDTO
                                .builder()
                                .authorizationType(AuthorizationTypeDTO.Admin)
                                .resource("/r/2")
                                .build(),
                        AuthorizationDTO
                                .builder()
                                .authorizationType(AuthorizationTypeDTO.Read)
                                .resource("/r/3")
                                .build()
                )
        );
        assertThat(rootServiceToken.isRoot()).isFalse();
        assertThat(rootServiceToken.hasAuthorizationOn(AuthorizationTypeDTO.Admin, "/r/1")).isTrue();
        assertThat(rootServiceToken.hasAuthorizationOn(AuthorizationTypeDTO.Write, "/r/2")).isTrue();
        assertThat(rootServiceToken.hasAuthorizationOn(AuthorizationTypeDTO.Admin, "/r/2")).isTrue();
        assertThat(rootServiceToken.hasAuthorizationOn(AuthorizationTypeDTO.Read, "/r/2")).isTrue();
        assertThat(rootServiceToken.hasAuthorizationOn(AuthorizationTypeDTO.Read, "/r/3")).isTrue();
        assertThat(rootServiceToken.hasAuthorizationOn(AuthorizationTypeDTO.Admin, "/r/3")).isFalse();
        assertThat(rootServiceToken.hasAuthorizationOn(AuthorizationTypeDTO.Write, "/r/3")).isFalse();
        assertThat(rootServiceToken.hasAuthorizationOn(AuthorizationTypeDTO.Read, "/r/1")).isTrue();
        assertThat(rootServiceToken.hasAuthorizationOn(AuthorizationTypeDTO.Write, "/r/1")).isTrue();
    }
}
