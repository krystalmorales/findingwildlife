package learn.findingwildlife.domain;

import learn.findingwildlife.data.SightingsRepository;
import learn.findingwildlife.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class SightingsServiceTest {

    @Autowired
    SightingsService service;

    @MockBean
    SightingsRepository repository;


    private void setupNationalPark() {
        Mockito
                .when(repository.findByNationalPark("Test"))
                .thenReturn(Arrays.asList(
                        new Sighting (1, makeOrganism(), makePark())
                ));
    }

    private void setupSighting() {
        Mockito
                .when(repository.findByOrganism("Gila monster"))
                .thenReturn(Arrays.asList(
                        new Sighting(1, makeOrganism(), makePark())
                ));
    }

    private void setupSightingDate() {
        Mockito
                .when(repository.findSightingByDate(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 30), 1))
                .thenReturn(Arrays.asList(
                        new Sighting(1, makeOrganism(), makePark(), makeDate())
                ));
    }

    private void setupSightingId() {
        Mockito
                .when(repository.findBySightingId(1))
                .thenReturn(Arrays.asList(
                        new Sighting(1, makeOrganism(), makePark())
                ));
    }

    private void setupUserId() {
        Mockito
                .when(repository.findByUserId(1))
                .thenReturn(Arrays.asList(
                        new Sighting(1, makeOrganism(), makePark(), 1)
                ));
    }

    @Test
    void shouldFindNationalPark() {
        setupNationalPark();

        List<Sighting> sightingList = service.findByNationalPark("Test");
        assertEquals(1, sightingList.size());
    }

    @Test
    void shouldFindOrganism() {
        setupSighting();

        List<Sighting> sightingList = service.findByOrganism("Gila monster");
        assertEquals(1, sightingList.size());
    }

    @Test
    void shouldFindByDate() {
        setupSightingDate();
        List<Sighting> sightingList = service.findSightingByDate(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 30), 1);
        assertEquals(1, sightingList.size());
    }

    @Test
    void ShouldFindSightingId() {
        setupSightingId();
        List<Sighting> sightingId = service.findBySightingId(1);
        assertEquals(1, sightingId.size());
    }

    @Test
    void shouldFindUserId() {
        setupUserId();
        List<Sighting> appUserId = service.findByUserId(1);
        assertEquals(1, appUserId.size());
    }
    @Test
    void shouldNotAddSighting() {
        Sighting sighting = makeSighting();
        Result<Sighting> result = service.createSighting(sighting);
        assertTrue(result.getErrorMessages().contains("ID cannot be set for operation"));

        sighting.setSightingId(0);
        sighting.setDate((LocalDate.now().minusDays(15)));
        result = service.createSighting(sighting);
        assertTrue(result.getErrorMessages().contains("Date to add sighting cannot be older than two weeks"));

        sighting.setSightingId(0);
        sighting.setDate((LocalDate.now().plusDays(5)));
        result = service.createSighting(sighting);
        assertTrue(result.getErrorMessages().contains("Date cannot be in the future"));

        sighting.setDate((LocalDate.now()));
        sighting.setTime((LocalTime.now().plusHours(1)));
        result = service.createSighting(sighting);
        assertTrue(result.getErrorMessages().contains("Time of sighting cannot be in the future for current sightings"));

        sighting.setTime((LocalTime.now()));
        sighting.setLatitude(0.0);
        result = service.createSighting(sighting);
        assertTrue(result.getErrorMessages().contains("You must enter latitude of sighting"));

        sighting.setLatitude(123.45);
        sighting.setLongitude(0.0);
        result = service.createSighting(sighting);
        assertTrue(result.getErrorMessages().contains("You must enter longitude of sighting"));

    }

    @Test
    void shouldAdd() {
        Sighting sighting = new Sighting();
        sighting.setDate((LocalDate.now()));
        sighting.setTime((LocalTime.now()));
        sighting.setComments("Test");
        sighting.setLatitude(45.02);
        sighting.setLongitude(-105.32);

        Sighting sightingToAdd = makeSighting();

        when(repository.createSighting(sighting)).thenReturn(sightingToAdd);

        Result<Sighting> actual = service.createSighting(sighting);
        assertEquals(sightingToAdd, actual.getPayload());
    }

    @Test
    void shouldNotUpdateNullSighting() {
        Sighting sighting = makeSighting();
        sighting.setSightingId(0);

        when(repository.updateSighting(sighting)).thenReturn(true);
        Result<Sighting> result = service.updateSighting(sighting);
        assertTrue(result.getErrorMessages().contains("sighting id must be set for update operation"));
    }

    @Test
    void shouldNotUpdateNullFields() {
        Sighting sighting = makeSighting();
        sighting.setDate((LocalDate.now().plusDays(1)));
        when(repository.updateSighting(sighting)).thenReturn(true);
        Result<Sighting> result = service.updateSighting(sighting);
        assertTrue(result.getErrorMessages().contains("Date cannot be in the future"));

        sighting.setDate((LocalDate.now()));
        sighting.setTime((LocalTime.now().plusMinutes(5)));
        result = service.updateSighting(sighting);
        assertTrue(result.getErrorMessages().contains("Time of sighting cannot be in the future for current sightings"));

        sighting.setTime((LocalTime.now()));
        sighting.setLatitude(0.0);
        result = service.updateSighting(sighting);
        assertTrue(result.getErrorMessages().contains("You must enter latitude of sighting"));

        sighting.setLatitude(123.45);
        sighting.setLongitude(0.0);
        result = service.updateSighting(sighting);
        assertTrue(result.getErrorMessages().contains("You must enter longitude of sighting"));

    }
    @Test
    void shouldUpdate() {
        Sighting sighting = makeSighting();
        sighting.setDate((LocalDate.of(2022, 9, 20)));
        sighting.setTime((LocalTime.of(20, 0)));
        sighting.setComments("Changed time, date, lat and long");
        sighting.setLatitude(15.36);
        sighting.setLongitude(-50.63);

        when(repository.updateSighting(sighting)).thenReturn(true);
        Result<Sighting> result = service.updateSighting(sighting);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldDelete() {
        when(repository.deleteSighting(2, 2)).thenReturn(true);
        assertTrue(service.deleteById(2, 2).isSuccess());
    }
    private Sighting makeSighting() {
        Sighting sighting = new Sighting();
        sighting.setSightingId(4);
        sighting.setDate((LocalDate.of(2020, 6, 13)));
        sighting.setTime((LocalTime.of(5, 45)));
        sighting.setComments("TEST");
        sighting.setLatitude(100.345);
        sighting.setLongitude(-78.098);
        sighting.setParkId(1);
        sighting.setOrganismId(1);
        sighting.setAppUserId(1);

        return sighting;
    }

    private Organism makeOrganism() {
        Organism organism = new Organism();
        organism.setOrganismId(4);
        organism.setCommonName("Gila monster");
        organism.setScientificName("Heloderma suspectum");
        organism.setCategory("Reptile");
        return organism;
    }


    private Park makePark() {
        Park park = new Park();
        park.setParkId(4);
        park.setParkName("Test");
        park.setCenterLatitude(20.32);
        park.setCenterLongitude(30.25);
        return park;
    }
    private LocalDate makeDate() {
        LocalDate date = LocalDate.of(2020, 6, 13);
        return date;
    }
}