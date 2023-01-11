package learn.findingwildlife.data;

import learn.findingwildlife.model.Organism;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrganismJdbcTemplateRepositoryTest {
    @Autowired
    OrganismJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByScientificName() {
        Organism organism = repository.findByScientificName("Ursus americanus");
        assertNotNull(organism);
        assertEquals("American Black Bear", organism.getCommonName());
    }

    @Test
    void shouldAdd() {
        Organism organism = new Organism();
        organism.setCommonName("cheetah");
        organism.setScientificName("Acinonyx jubatus");
        organism.setCategory("mammal");

        Organism actual = repository.createOrganism(organism);
        assertNotNull(actual);
        assertEquals(4, actual.getOrganismId());
    }

    @Test
    void shouldUpdate() {
        Organism organism = new Organism();
        organism.setOrganismId(1);
        organism.setCategory("Bear");
        organism.setCommonName("American Black Bear");
        organism.setScientificName("Ursus americanus");
        assertTrue(repository.updateOrganism(organism));
        assertEquals("Bear", organism.getCategory());
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteOrganism(2));
    }

    @Test
    void shouldNotDeleteIfNonExistent() {
        assertFalse(repository.deleteOrganism(15));
    }

    @Test
    void shouldFindOrganismReferenceWithValidId() {
        assertTrue(repository.findAnyReference(1));
    }

    @Test
    void shouldNotFindOrganismWithNonExistentId() {
        assertFalse(repository.findAnyReference(15));
    }
}