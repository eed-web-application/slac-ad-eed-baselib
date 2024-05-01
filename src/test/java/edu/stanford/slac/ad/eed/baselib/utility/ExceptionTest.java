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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureMockMvc
@SpringBootTest()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
public class ExceptionTest {
    @MockBean
    AuthService authService;

    @Test
    public void testExceptionStandardMessage() {
        ControllerLogicException ex = ControllerLogicException
                .builder()
                .errorCode(-1)
                .errorMessage("test exception")
                .errorDomain("testExceptionDomain")
                .build();
        assertThat(ex.getErrorMessage()).isEqualTo(ex.getMessage());
    }
}
