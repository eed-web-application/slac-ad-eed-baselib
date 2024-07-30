package edu.stanford.slac.ad.eed.baselib.service;

import edu.stanford.slac.ad.eed.baselib.api.v1.dto.PersonQueryParameterDTO;
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
public class PeopleGroupServiceTest {
    @MockBean
    AuthService authService;
    @Autowired
    PeopleGroupService peopleGroupService;

    @Test
    public void findAllTest() {
        var allPerson =  peopleGroupService.findPersons("");
        assertThat(allPerson).isNotNull();
    }

    @Test
    public void findPersonPaging() {
        var allPersonFirstSearch =  peopleGroupService.findPersons(
                PersonQueryParameterDTO
                        .builder()
                        .limit(10)
                        .build()
        );
        assertThat(allPersonFirstSearch).isNotNull().hasSize(10);
        assertThat(allPersonFirstSearch.get(0).mail()).isNotNull().isEqualTo("user01@slac.stanford.edu");
        assertThat(allPersonFirstSearch.get(1).mail()).isNotNull().isEqualTo("user02@slac.stanford.edu");
        assertThat(allPersonFirstSearch.get(9).mail()).isNotNull().isEqualTo("user10@slac.stanford.edu");
        var allPersonSecond =  peopleGroupService.findPersons(
                PersonQueryParameterDTO
                        .builder()
                        .anchor(allPersonFirstSearch.get(9).mail())
                        .limit(5)
                        .build()
        );
        assertThat(allPersonSecond).isNotNull().hasSize(5);
        assertThat(allPersonSecond.get(0).mail()).isNotNull().isEqualTo("user11@slac.stanford.edu");
        assertThat(allPersonSecond.get(4).mail()).isNotNull().isEqualTo("user15@slac.stanford.edu");
        var allPersonContextAndLimit =  peopleGroupService.findPersons(
                PersonQueryParameterDTO
                        .builder()
                        .anchor(allPersonFirstSearch.get(9).mail())
                        .context(2)
                        .limit(2)
                        .build()
        );
        assertThat(allPersonContextAndLimit).isNotNull().hasSize(3);
        assertThat(allPersonContextAndLimit.get(0).mail()).isNotNull().isEqualTo("user09@slac.stanford.edu");
        assertThat(allPersonContextAndLimit.get(1).mail()).isNotNull().isEqualTo("user10@slac.stanford.edu");
        assertThat(allPersonContextAndLimit.get(2).mail()).isNotNull().isEqualTo("user11@slac.stanford.edu");
        assertThat(allPersonContextAndLimit.get(3).mail()).isNotNull().isEqualTo("user12@slac.stanford.edu");
    }
}
