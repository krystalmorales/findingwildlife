package learn.findingwildlife.data;

import learn.findingwildlife.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SightingsJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 4;

    @Autowired
    SightingsJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByDate(){
        List<Sighting> sightings = repository.findSightingByDate(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 30), 2);
        assertEquals(2, sightings.size());

    }

    @Test
    void shouldFindYellowstoneNationalPark() {
        List<Sighting> sightings = repository.findByNationalPark("Yellowstone National Park");
        assertEquals(1, sightings.size());

        sightings = repository.findByNationalPark("YELLOWSTONE NATIONAL PARK");
        assertEquals(1, sightings.size());


        sightings = repository.findByNationalPark("Acadia");
        assertEquals(0, sightings.size());

    }

    @Test
    void shouldFindAmericanBlackBear() {
        List<Sighting> blackBear = repository.findByOrganism("American black bear");
        assertEquals(1, blackBear.size());

        blackBear = repository.findByOrganism("AMERICAN BLACK BEAR");
        assertEquals(1, blackBear.size());

        blackBear = repository.findByOrganism("lizard");
        assertEquals(0, blackBear.size());

    }

    @Test
    void shouldFindBySightingId() {
        List<Sighting> sightingOne = repository.findBySightingId(1);
        assertEquals(1, sightingOne.size());
    }

    @Test
    void shouldFindAppUserId(){
        List<Sighting> appUserId = repository.findByUserId(1);
        assertEquals(1, appUserId.size());
    }
    @Test
    void shouldAdd() {
        Sighting sighting = makeSighting();
        Sighting actual = repository.createSighting(sighting);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getSightingId());
    }

    @Test
    void shouldUpdate() {
        Sighting sighting = updatedSighting();
        sighting.setSightingId(1);
        sighting.setAppUserId(1);

        assertTrue(repository.updateSighting(sighting));
    }

    private Sighting updatedSighting() {
        Sighting sighting = new Sighting();
        sighting.setDate((LocalDate.of(2020, 6, 13)));
        sighting.setTime((LocalTime.of(5, 45)));
        sighting.setComments(null);
        sighting.setLatitude(100.345);
        sighting.setLongitude(-78.098);
        sighting.setParkId(2);
        sighting.setOrganismId(2);
        return sighting;
    }


    private Sighting makeSighting() {
        Sighting sighting = new Sighting();
        sighting.setDate((LocalDate.of(2020, 06, 13)));
        sighting.setTime((LocalTime.of(05, 45)));
        sighting.setComments("TEST");
        sighting.setLatitude(100.345);
        sighting.setLongitude(-78.098);
        sighting.setParkId(1);
        sighting.setOrganismId(1);
        return sighting;
    }


    @Test
    void shouldNotDeleteIfNonExistent() {
        assertFalse(repository.deleteSighting(10, 3));

    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteSighting(2, 2));
    }
}