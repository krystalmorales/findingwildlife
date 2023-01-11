package learn.findingwildlife.data;

import learn.findingwildlife.model.ParkOrganism;

import java.util.List;

public interface ParkOrganismRepository {
    List<ParkOrganism> findByParkId(int parkId);
    ParkOrganism createParkOrganism(ParkOrganism parkOrganism);
    boolean updateParkOrganism(ParkOrganism parkOrganism);
}
