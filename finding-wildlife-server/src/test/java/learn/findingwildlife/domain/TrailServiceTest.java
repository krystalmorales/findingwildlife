package learn.findingwildlife.domain;

import learn.findingwildlife.data.TrailRepository;
import learn.findingwildlife.model.Trail;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrailServiceTest {

    @Autowired
    TrailService service;
    @MockBean
    TrailRepository repository;

    private void setupTrailName() {
        Mockito
                .when(repository.findAll())
                .thenReturn(Arrays.asList(
                        new Trail(1, "YS one", 1)
                ));
    }

    @Test
    void shouldFindTrailName() {
        setupTrailName();

        List<Trail> trail = service.findAll();
        assertNotNull(trail);
        assertEquals(1, trail.size());
    }
}