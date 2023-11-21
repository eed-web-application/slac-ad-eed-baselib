package edu.stanford.slac.ad.eed.baselib.utility;

import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureMockMvc
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
public class StringUtilityTest {
    @MockBean
    AuthService authService;
    @Test
    public void testStringNormalization() {
        String nromalizedString = StringUtilities.normalizeStringWithReplace(
                "Ğo Ťo ânťařēş",
                " ",
                "-"
        );

        assertThat(nromalizedString)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("go-to-antares");
    }
}
