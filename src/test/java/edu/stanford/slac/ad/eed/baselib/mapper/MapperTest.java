package edu.stanford.slac.ad.eed.baselib.mapper;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.AuthenticationTokenDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.dto.NewAuthenticationTokenDTO;
import edu.stanford.slac.ad.eed.baselib.api.v1.mapper.AuthMapper;
import edu.stanford.slac.ad.eed.baselib.api.v1.mapper.ModelChangeMapper;
import edu.stanford.slac.ad.eed.baselib.model.ModelChange;
import edu.stanford.slac.ad.eed.baselib.model.ModelChangesHistory;
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
public class MapperTest {
    @Autowired
    AuthMapper authMapper;
    @Autowired
    ModelChangeMapper modelChangeMapper;
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

    @Test
    public void testModelChangeMapper() {
        var modelChangesHistoryDTO = modelChangeMapper.toDTO(
                ModelChangesHistory
                        .builder()
                        .changes(
                                List.of(
                                        ModelChange
                                                .builder()
                                                .fieldName("field-name-1")
                                                .oldValue("old-value")
                                                .newValue("new-value")
                                                .build(),
                                        ModelChange
                                                .builder()
                                                .fieldName("field-name-2")
                                                .oldValue(Integer.valueOf(1))
                                                .newValue(Integer.valueOf(2))
                                                .build()
                                )
                        )
                        .build()
        );
        assertThat(modelChangesHistoryDTO.changes().size()).isEqualTo(2);
        assertThat(modelChangesHistoryDTO.changes().get(0).fieldName()).isEqualTo("field-name-1");
        assertThat(modelChangesHistoryDTO.changes().get(0).oldValue()).isEqualTo("old-value");
        assertThat(modelChangesHistoryDTO.changes().get(0).newValue()).isEqualTo("new-value");
        assertThat(modelChangesHistoryDTO.changes().get(1).fieldName()).isEqualTo("field-name-2");
        assertThat(modelChangesHistoryDTO.changes().get(1).oldValue()).isEqualTo("1");
        assertThat(modelChangesHistoryDTO.changes().get(1).newValue()).isEqualTo("2");
    }
}
