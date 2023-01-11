package learn.findingwildlife.data;

import learn.findingwildlife.model.Organism;


public interface OrganismRepository {
    Organism findByScientificName(String scientificName);
    Organism createOrganism(Organism organism);
    boolean updateOrganism(Organism organism);
    boolean deleteOrganism(int organismId);
    boolean findAnyReference(int organismId);
}
