package learn.findingwildlife.domain;

import learn.findingwildlife.data.SightingsRepository;
import learn.findingwildlife.model.Sighting;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class SightingsService {
    private final SightingsRepository repository;

    public SightingsService(SightingsRepository repository) {
        this.repository = repository;
    }

    public static LocalDate getMaxDate() {
        return LocalDate.now();
    }

    public List<Sighting> findSightingByDate(LocalDate date, LocalDate dateTwo, int parkId) {
        return repository.findSightingByDate(date, dateTwo, parkId);
    }
    public List<Sighting> findByNationalPark (String parkName) {
        return repository.findByNationalPark(parkName);
    }

    public List<Sighting> findByOrganism (String organismCommonName) {
        return repository.findByOrganism(organismCommonName);
    }

    public List<Sighting> findBySightingId(int sightingId) {
        return repository.findBySightingId(sightingId);
    }

    public List<Sighting> findByUserId(int userId) {
        return repository.findByUserId(userId);
    }

    public Result<Sighting> createSighting(Sighting sighting) {
        Result<Sighting> result = validate(sighting);

        if(!result.isSuccess()) {
            return result;
        }
        if (sighting.getSightingId() != 0) {
            result.addErrorMessage("ID cannot be set for operation");
            return result;
        }

        if (sighting.getDate().isBefore((LocalDate.now().minusWeeks(2)))) {
            result.addErrorMessage("Date to add sighting cannot be older than two weeks");
        }

        sighting = repository.createSighting(sighting);
        result.setPayload(sighting);
        return result;

    }

    public Result<Sighting> updateSighting (Sighting sighting) {
        Result<Sighting> result = validate(sighting);

        if(!result.isSuccess()) {
            return result;
        }
        if(sighting.getSightingId() <= 0) {
            result.addErrorMessage("sighting id must be set for update operation");
        }
        if(!repository.updateSighting(sighting)) {
            String msg = String.format("sighting id: %s, not found", sighting.getSightingId());
            result.addErrorMessage(msg);
        }
        if(!repository.updateSighting(sighting)) {
            String msg = String.format("app user id: %s, not found", sighting.getAppUserId());
            result.addErrorMessage(msg);
        }
        return result;
    }

    public Result<Sighting> deleteById(int sightingId, int appUserId) {
        Result<Sighting> result = new Result<>();
        if(!repository.deleteSighting(sightingId, appUserId)) {
            result.addErrorMessage(String.format("Sighting ID %d does not exist", sightingId));
        }

        return result;
    }

    private Result<Sighting> validate( Sighting sighting) {
        Result<Sighting> result = new Result<>();
        if (sighting == null) {
            result.addErrorMessage("Sighting cannot be null");
            return result;
        }
        if (sighting.getDate().isAfter(getMaxDate())) {
            result.addErrorMessage("Date cannot be in the future");
        }

        if (sighting.getTime().isAfter((LocalTime.now())) && Objects.equals(sighting.getDate(), getMaxDate())) {
            result.addErrorMessage("Time of sighting cannot be in the future for current sightings");
        }
        if (sighting.getLatitude() <= 0) {
            result.addErrorMessage("You must enter latitude of sighting");
        }
        if (sighting.getLongitude() >= 0) {
            result.addErrorMessage("You must enter longitude of sighting");
        }
        return result;
    }

}


