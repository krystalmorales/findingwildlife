package learn.findingwildlife.data;

import learn.findingwildlife.data.ParkJdbcTemplateRepository;
import learn.findingwildlife.model.Park;
import learn.findingwildlife.model.Trail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParkJdbcTemplateRepositoryTest {

    @Autowired
    ParkJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAllParks() {
        List<Park> allParks = repository.findAll();
        assertNotNull(allParks);
        assertEquals(3, allParks.size());
        assertEquals("Yellowstone National Park", allParks.get(0).getParkName());
    }

    @Test
    void shouldFindAllTrailsInYellowstone() {
        Park park = repository.findByParkId(1);
        List<Trail> trailsInYellowstone = park.getTrails();
        assertNotNull(trailsInYellowstone);
        assertEquals(2, trailsInYellowstone.size());
        assertEquals("YS one", trailsInYellowstone.get(0).getTrailName());
    }
}