package learn.findingwildlife.data;

import learn.findingwildlife.model.Sighting;

import java.time.LocalDate;
import java.util.List;

public interface SightingsRepository {
    List<Sighting> findSightingByDate(LocalDate date, LocalDate dateTwo, int parkId);
    List<Sighting> findByNationalPark(String parkName);
    List<Sighting> findByOrganism(String organismCommonName);
    List<Sighting> findBySightingId(int sightingId);
    List<Sighting> findByUserId(int userId);
    Sighting createSighting(Sighting sighting);
    boolean updateSighting(Sighting sighting);
    boolean deleteSighting(int sightingId, int app_user_id);
}
