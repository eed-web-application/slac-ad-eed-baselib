package edu.stanford.slac.ad.eed.baselib.utility;

import edu.stanford.slac.ad.eed.baselib.exception.ControllerLogicException;
import edu.stanford.slac.ad.eed.baselib.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static edu.stanford.slac.ad.eed.baselib.exception.Utility.wrapCatch;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureMockMvc
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
public class WrapperTest {
    @MockBean
    AuthService authService;

    @Test
    public void testWrapCatchAutomaticDomain() {
        ControllerLogicException exception = assertThrows(
                ControllerLogicException.class,
                () -> wrapCatch(
                        () -> {
                            throw new RuntimeException("test exception");
                        },
                        -1
                )
        );
        assertThat(exception.getErrorMessage())
                .isNotNull()
                .contains("test exception");
        assertThat(exception.getErrorDomain())
                .isNotNull()
                .contains("testWrapCatchAutomaticDomain");
    }
}
