package learn.findingwildlife.data;

import learn.findingwildlife.model.Trail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrailJdbcTemplateRepositoryTest {

    @Autowired
    TrailJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {knownGoodState.set();}

    @Test
    void shouldFindTrailName(){
        List<Trail> trail = repository.findAll();
        assertEquals(4, trail.size());
    }
}