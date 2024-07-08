package edu.stanford.slac.ad.eed.baselib.controller;

import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import org.assertj.core.api.AssertionsForClassTypes;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
public class BadgeControllerTest {
    @MockBean
    AuthService authService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void fetchQueryParameter() throws Exception {
        MvcResult get_result = mockMvc.perform(
                        get("/v1/badge/version")
                                .contentType("image/svg+xml")
                )
                .andExpect(status().isOk())
                .andReturn();
        String badge_svg = get_result.getResponse().getContentAsString();
        AssertionsForClassTypes.assertThat(badge_svg).isNotNull();
    }
}