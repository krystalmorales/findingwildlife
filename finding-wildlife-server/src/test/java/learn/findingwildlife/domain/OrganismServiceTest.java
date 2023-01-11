package learn.findingwildlife.domain;

import learn.findingwildlife.data.OrganismRepository;
import learn.findingwildlife.model.Organism;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrganismServiceTest {

    @Autowired
    OrganismService service;

    @MockBean
    OrganismRepository repository;

    @Test
    void shouldFindBlackBearByScientificName() {
        Organism expected = makeOrganism();
        when(repository.findByScientificName("ursus americanus")).thenReturn(expected);
        Result<Organism> actual = service.findByScientificName("ursus americanus");
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldReturnErrorWithNullScientificName() {
        Result<Organism> actual = service.findByScientificName(null);
        assertTrue(actual.getErrorMessages().contains("Scientific name is required."));
    }

    @Test
    void shouldCreateOrganism() {
       Organism organism = new Organism();
       organism.setCommonName("American Black Bear");
       organism.setScientificName("ursus americanus");
       organism.setCategory("mammal");

       Organism afterRepoAdd = makeOrganism();

       when(repository.createOrganism(organism)).thenReturn(afterRepoAdd);

       Result<Organism> actual = service.createOrganism(organism);
       assertEquals(afterRepoAdd, actual.getPayload());
    }

    @Test
    void shouldNotCreateInvalidOrganism() {
        Organism organism = makeOrganism();
        Result<Organism> result = service.createOrganism(organism);
        assertTrue(result.getErrorMessages().contains("ID cannot be set for operation."));

        organism.setOrganismId(0);
        organism.setScientificName(null);
        result = service.createOrganism(organism);
        assertTrue(result.getErrorMessages().contains("Scientific name is required"));

        organism.setScientificName("ursus americanus");
        organism.setCommonName(null);
        result = service.createOrganism(organism);
        assertTrue(result.getErrorMessages().contains("Common name is required"));

        organism.setCommonName("American Black Bear");
        organism.setCategory(null);
        result = service.createOrganism(organism);
        assertTrue(result.getErrorMessages().contains("Category is required"));
    }

    @Test
    void shouldUpdateOrganism() {
        Organism organism = makeOrganism();
        organism.setCategory("Bear");

        when(repository.updateOrganism(organism)).thenReturn(true);
        Result<Organism> result = service.updateOrganism(organism);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateToNullFields() {
        Organism organism = makeOrganism();
        organism.setCategory(null);

        when(repository.updateOrganism(organism)).thenReturn(true);
        Result<Organism> result = service.updateOrganism(organism);
        assertTrue(result.getErrorMessages().contains("Category is required"));

        organism.setCategory("mammal");
        organism.setCommonName(null);
        result = service.updateOrganism(organism);
        assertTrue(result.getErrorMessages().contains("Common name is required"));

        organism.setCommonName("American Black Bear");
        organism.setScientificName(null);
        result = service.updateOrganism(organism);
        assertTrue(result.getErrorMessages().contains("Scientific name is required"));
    }

    @Test
    void shouldNotUpdateWithInvalidId() {
        Organism organism = makeOrganism();
        organism.setOrganismId(0);

        when(repository.updateOrganism(organism)).thenReturn(true);
        Result<Organism> result = service.updateOrganism(organism);
        assertTrue(result.getErrorMessages().contains("Organism ID must be set for 'update' operation."));
    }

    @Test
    void shouldDelete() {
        when(repository.deleteOrganism(2)).thenReturn(true);
        assertTrue(service.deleteById(2).isSuccess());
    }

    @Test
    void shouldNotDeleteInUseOrganism() {
        when(repository.deleteOrganism(1)).thenReturn(true);
        when(repository.findAnyReference(1)).thenReturn(true);
        assertTrue(service.deleteById(1)
                .getErrorMessages()
                .contains("Organism ID 1 in use. Must delete all references before deleting Organism."));
    }

    Organism makeOrganism() {
        Organism organism = new Organism();
        organism.setOrganismId(1);
        organism.setCommonName("American Black Bear");
        organism.setScientificName("ursus americanus");
        organism.setCategory("mammal");
        return organism;
    }
}