package learn.findingwildlife.domain;


import learn.findingwildlife.data.OrganismRepository;
import learn.findingwildlife.model.Organism;
import org.hibernate.validator.internal.metadata.PredefinedScopeBeanMetaDataManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganismService {

    private final OrganismRepository repository;

    public OrganismService(OrganismRepository repository) {
        this.repository = repository;
    }

    public Result<Organism> findByScientificName(String scientificName) {
        Result<Organism> result = new Result<>();

        if (scientificName == null) {
            result.addErrorMessage("Scientific name is required.");
            return result;
        }

        Organism organism = repository.findByScientificName(scientificName);
        result.setPayload(organism);
        return result;
    }

    public Result<Organism> createOrganism(Organism organism) {
        Result<Organism> result = validateOrganism(organism);

        if (!result.isSuccess()) {
            return result;
        }

        if (organism.getOrganismId() != 0) {
            result.addErrorMessage("ID cannot be set for operation.");
            return result;
        }

        organism = repository.createOrganism(organism);
        result.setPayload(organism);
        return result;
    }

    public Result<Organism> updateOrganism(Organism organism) {
        Result<Organism> result = new Result<>();
        result = validateNulls(result, organism);
        if(!result.isSuccess()) {
            return result;
        }

        if (organism.getOrganismId() <= 0) {
            result.addErrorMessage("Organism ID must be set for 'update' operation.");
            return result;
        }

        if (!repository.updateOrganism(organism)) {
            result.addErrorMessage(String.format("Organism with ID %s not found", organism.getOrganismId()));
        }

        return result;
    }

    public Result<Organism> deleteById(int organismId) {
        Result<Organism> result = new Result<>();

        boolean referenced = repository.findAnyReference(organismId);

        if (!referenced) {
            if (!repository.deleteOrganism(organismId)) {
                result.addErrorMessage(String.format("Organism ID %d does not exist", organismId));
            }

            return result;
        }

        result.addErrorMessage(String.format("Organism ID %d in use. Must delete all references before deleting Organism.", organismId));
        return result;
    }

    private Result<Organism> validateNulls(Result<Organism> result, Organism organism) {
        if (organism == null) {
            result.addErrorMessage("Organism cannot be null");
            return result;
        }

        if (organism.getScientificName() == null || organism.getScientificName().isBlank()) {
            result.addErrorMessage("Scientific name is required");
        }

        if (organism.getCommonName() == null || organism.getCommonName().isBlank()) {
            result.addErrorMessage("Common name is required");
        }

        if (organism.getCategory() == null || organism.getCategory().isBlank()) {
            result.addErrorMessage("Category is required");
        }

        return result;
    }

    private Result<Organism> validateOrganism(Organism organism) {
        Result<Organism> result = new Result<>();

        Organism existing = repository.findByScientificName(organism.getScientificName());
        if (existing != null) {
            result.addErrorMessage("An organism with that already exists.");
        }

        result = validateNulls(result, organism);

        return result;
    }
}
