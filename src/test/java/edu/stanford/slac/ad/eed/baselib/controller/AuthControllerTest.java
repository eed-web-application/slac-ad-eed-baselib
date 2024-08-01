package edu.stanford.slac.ad.eed.baselib.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationOwnerTypeDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthorizationTypeDTO;
import edu.stanford.slac.ad.eed.baselib.auth.JWTHelper;
import edu.stanford.slac.ad.eed.baselib.config.AppProperties;
import edu.stanford.slac.ad.eed.baselib.controller.dto.TokenTesterInfo;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
public class AuthControllerTest {
    @MockBean
    AuthService authService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    AppProperties appProperties;
    @Autowired
    JWTHelper jwtHelper;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testFetchToken() throws Exception {
        // Mock the getAllAuthenticationForOwner method
        when(authService.getAllAuthenticationForOwner(
                "user1",
                AuthorizationOwnerTypeDTO.User,
                Optional.of(false))
        ).thenReturn(
                List.of(
                        AuthorizationDTO
                                .builder()
                                .resource("*")
                                .authorizationType(AuthorizationTypeDTO.Admin)
                                .build()
                )
        );

        MvcResult result = mockMvc.perform(
                        get("/v1/auth/test/token")
                                .header(appProperties.getUserHeaderName(), jwtHelper.generateJwt("user1"))
                )
                .andExpect(status().isOk())
                .andReturn();
        TokenTesterInfo testerInfo = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        assertThat(testerInfo).isNotNull();
        assertThat(testerInfo.isAuthenticated).isTrue();
        assertThat(testerInfo.isImpersonating).isFalse();
        assertThat(testerInfo.principal).isEqualTo("user1");
        assertThat(testerInfo.sourcePrincipal).isEqualTo("user1");
        assertThat(testerInfo.authorizationDTOS).isNotNull().hasSize(1);
        assertThat(testerInfo.authorizationDTOS.get(0).resource()).isEqualTo("*");
        assertThat(testerInfo.authorizationDTOS.get(0).authorizationType()).isEqualTo(AuthorizationTypeDTO.Admin);
    }

    @Test
    public void testFetchTokenDuringImpersonation() throws Exception {
        // Mock the getAllAuthenticationForOwner method
        when(authService.checkForRoot("user1")).thenReturn(true);
        when(authService.getAllAuthenticationForOwner(
                "user1",
                AuthorizationOwnerTypeDTO.User,
                Optional.of(false))
        ).thenReturn(
                List.of(
                        AuthorizationDTO
                                .builder()
                                .resource("*")
                                .authorizationType(AuthorizationTypeDTO.Admin)
                                .build()
                )
        );

        when(authService.checkForRoot("user2")).thenReturn(false);
        when(authService.getAllAuthenticationForOwner(
                "user2",
                AuthorizationOwnerTypeDTO.User,
                Optional.of(false))
        ).thenReturn(
                List.of(
                        AuthorizationDTO
                                .builder()
                                .resource("imp/resource")
                                .authorizationType(AuthorizationTypeDTO.Write)
                                .build()
                )
        );


        MvcResult result = mockMvc.perform(
                        get("/v1/auth/test/token")
                                .header(appProperties.getUserHeaderName(), jwtHelper.generateJwt("user1"))
                                .header(appProperties.getImpersonateHeaderName(), "user2")
                )
                .andExpect(status().isOk())
                .andReturn();
        TokenTesterInfo testerInfo = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        assertThat(testerInfo).isNotNull();
        assertThat(testerInfo.isAuthenticated).isTrue();
        assertThat(testerInfo.isImpersonating).isTrue();
        assertThat(testerInfo.principal).isEqualTo("user2");
        assertThat(testerInfo.credential).isEqualTo("user2");
        assertThat(testerInfo.sourcePrincipal).isEqualTo("user1");
        assertThat(testerInfo.authorizationDTOS).isNotNull().hasSize(1);
        assertThat(testerInfo.authorizationDTOS.get(0).resource()).isEqualTo("imp/resource");
        assertThat(testerInfo.authorizationDTOS.get(0).authorizationType()).isEqualTo(AuthorizationTypeDTO.Write);

    }
}
