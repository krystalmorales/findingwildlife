package learn.findingwildlife.domain;



import learn.findingwildlife.data.ParkOrganismRepository;
import learn.findingwildlife.domain.Result;
import learn.findingwildlife.model.AppUser;
import learn.findingwildlife.model.ParkOrganism;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkOrganismService {

    private final ParkOrganismRepository repository;

    public ParkOrganismService(ParkOrganismRepository repository) {
        this.repository = repository;
    }

    public List<ParkOrganism> findByParkId(int parkId) {
        return repository.findByParkId(parkId);
    }

    public Result<ParkOrganism> createParkOrganism(ParkOrganism parkOrganism) {
        Result<ParkOrganism> result = validate(parkOrganism);

        if (!result.isSuccess()) {
            return result;
        }

        result = validateNotDuplicated(parkOrganism);

        if (!result.isSuccess()) {
            return result;
        }

        if (parkOrganism.getParkOrganismId() != 0) {
            result.addErrorMessage("ParkOrganism cannot be set for 'add' operation.");
            return result;
        }

        parkOrganism = repository.createParkOrganism(parkOrganism);
        result.setPayload(parkOrganism);
        return result;
    }

    public Result<ParkOrganism> updateParkOrganism(ParkOrganism parkOrganism) {
        Result<ParkOrganism> result = validate(parkOrganism);

        if (!result.isSuccess()) {
            return result;
        }

        if (parkOrganism.getParkOrganismId() <= 0) {
            result.addErrorMessage("ParkOrganism ID must be appropriately set for `update` operation.");
            return result;
        }

        if (!repository.updateParkOrganism(parkOrganism)) {
            result.addErrorMessage(String.format("ParkOrganism with ID %s was not found.", parkOrganism.getParkOrganismId()));
        }

        return result;
    }

    private Result<ParkOrganism> validate(ParkOrganism parkOrganism) {
        Result<ParkOrganism> result = new Result<>();

        if (parkOrganism == null) {
            result.addErrorMessage("ParkOrganism cannot be null");
            return result;
        }

        if (parkOrganism.getOrganism() == null) {
            result.addErrorMessage("Organism is required and cannot be null.");
        }

        if (parkOrganism.getParkId() == 0) {
            result.addErrorMessage("Park must be specified.");
        }

        if (parkOrganism.getNativeness().getName().isBlank()) {
            result.addErrorMessage("Nativeness must be specified");
        }

        return result;
    }

    private Result<ParkOrganism> validateNotDuplicated(ParkOrganism parkOrganism) {
        Result<ParkOrganism> result = new Result<>();

        List<ParkOrganism> existing = repository.findByParkId(parkOrganism.getParkId());
        for (ParkOrganism exists: existing) {
            if (parkOrganism.getOrganism().getOrganismId() == exists.getOrganism().getOrganismId()) {
                result.addErrorMessage("This organism already exists in the database for this park.");
                return result;
            }
        }

        return result;
    }
}
