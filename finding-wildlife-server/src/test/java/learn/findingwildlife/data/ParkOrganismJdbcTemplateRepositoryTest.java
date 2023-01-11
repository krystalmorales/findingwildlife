package learn.findingwildlife.data;


import learn.findingwildlife.data.ParkOrganismJdbcTemplateRepository;
import learn.findingwildlife.model.Nativeness;
import learn.findingwildlife.model.Organism;
import learn.findingwildlife.model.ParkOrganism;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParkOrganismJdbcTemplateRepositoryTest {

    @Autowired
    ParkOrganismJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindParkOrganismsGivenParkId() {
        List<ParkOrganism> allParkOrganismsInJoshuaTree = repository.findByParkId(3);
        assertNotNull(allParkOrganismsInJoshuaTree);
        assertEquals(1, allParkOrganismsInJoshuaTree.size());
        assertEquals(3, allParkOrganismsInJoshuaTree.get(0).getParkOrganismId());
    }

    @Test
    void shouldCreateParkOrganism() {
        ParkOrganism parkOrganism = makeParkOrganism();

        ParkOrganism actual = repository.createParkOrganism(parkOrganism);

        assertNotNull(actual);
        assertEquals(4, actual.getParkOrganismId());
    }

    @Test
    void shouldUpdateParkOrganism() {
        ParkOrganism parkOrganism = makeParkOrganism();
        parkOrganism.setAbundance("Common");
        assertTrue(repository.updateParkOrganism(parkOrganism));
    }

    private ParkOrganism makeParkOrganism() {
        ParkOrganism parkOrganism = new ParkOrganism();
        Organism organism = makeOrganism();

        parkOrganism.setOrganism(organism);
        parkOrganism.setParkId(1);
        parkOrganism.setNativeness(Nativeness.findByName("Native"));
        parkOrganism.setAbundance("Rare");

        return parkOrganism;
    }
    private Organism makeOrganism() {
        Organism organism = new Organism();
        organism.setOrganismId(1);
        organism.setCommonName("American black bear");
        organism.setScientificName("ursus americanus");
        organism.setCategory("mammal");

        return organism;
    }
}