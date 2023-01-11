package learn.findingwildlife.domain;

import learn.findingwildlife.data.ParkOrganismRepository;

import learn.findingwildlife.model.Nativeness;
import learn.findingwildlife.model.Organism;
import learn.findingwildlife.model.ParkOrganism;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ParkOrganismServiceTest {

    @Autowired
    ParkOrganismService service;

    @MockBean
    ParkOrganismRepository repository;

    @Test
    void shouldCreateParkOrganism() {
        ParkOrganism parkOrganism = makeParkOrganism();

        ParkOrganism mockParkOrganism = makeParkOrganism();
        mockParkOrganism.setParkOrganismId(4);

        when(repository.createParkOrganism(parkOrganism)).thenReturn(mockParkOrganism);

        Result<ParkOrganism> result = service.createParkOrganism(parkOrganism);
        assertTrue(result.isSuccess());
        assertEquals(mockParkOrganism, result.getPayload());
    }

    @Test
    void shouldNotCreateWithNullField() {
        ParkOrganism parkOrganism = makeParkOrganism();
        parkOrganism.setParkId(0);

        ParkOrganism mockParkOrganism = makeParkOrganism();
        mockParkOrganism.setParkOrganismId(4);
        mockParkOrganism.setParkId(0);

        when(repository.createParkOrganism(parkOrganism)).thenReturn(mockParkOrganism);

        Result<ParkOrganism> result = service.createParkOrganism(parkOrganism);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("Park must be specified."));

        parkOrganism.setParkId(1);
        parkOrganism.setOrganism(null);
        result = service.createParkOrganism(parkOrganism);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("Organism is required and cannot be null."));

        parkOrganism = null;
        result = service.createParkOrganism(parkOrganism);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("ParkOrganism cannot be null"));
    }

    @Test
    void shouldNotCreateDuplicateOrganismInSamePark() {
        Organism existingOrganism = new Organism();
        existingOrganism.setOrganismId(1);

        ParkOrganism parkOrganism = new ParkOrganism();
        parkOrganism.setAbundance("Uncommon");
        parkOrganism.setNativeness(Nativeness.findByName("Native"));
        parkOrganism.setParkId(1);
        parkOrganism.setOrganism(existingOrganism);

        ParkOrganism mockParkOrganism = parkOrganism;
        mockParkOrganism.setParkOrganismId(4);

        List<ParkOrganism> existing = new ArrayList<>();
        existing.add(parkOrganism);

        when(repository.createParkOrganism(parkOrganism)).thenReturn(mockParkOrganism);
        when(repository.findByParkId(1)).thenReturn(existing);

        Result<ParkOrganism> result = service.createParkOrganism(parkOrganism);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("This organism already exists in the database for this park."));
    }

    @Test
    void shouldUpdateParkOrganism() {
        ParkOrganism parkOrganism = makeParkOrganism();
        parkOrganism.setAbundance("Common");
        parkOrganism.setParkOrganismId(4);

        when(repository.updateParkOrganism(parkOrganism)).thenReturn(true);
        Result<ParkOrganism> result = service.updateParkOrganism(parkOrganism);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateToNullFields() {
        ParkOrganism parkOrganism = makeParkOrganism();
        parkOrganism.setOrganism(null);
        parkOrganism.setParkOrganismId(4);

        when(repository.updateParkOrganism(parkOrganism)).thenReturn(true);
        Result<ParkOrganism> result = service.updateParkOrganism(parkOrganism);
        assertFalse(result.isSuccess());

        parkOrganism = null;
        result = service.updateParkOrganism(parkOrganism);
        assertFalse(result.isSuccess());
    }

    ParkOrganism makeParkOrganism() {
        ParkOrganism parkOrganism = new ParkOrganism();
        parkOrganism.setNativeness(Nativeness.findByName("Native"));
        parkOrganism.setParkId(1);
        parkOrganism.setAbundance("Rare");

        Organism organism = makeOrganism();
        parkOrganism.setOrganism(organism);

        return parkOrganism;
    }

    Organism makeOrganism() {
        Organism organism = new Organism();
        organism.setOrganismId(1);
        organism.setScientificName("felis catus");
        organism.setCommonName("cat");
        organism.setCategory("mammal");

        return organism;
    }
}