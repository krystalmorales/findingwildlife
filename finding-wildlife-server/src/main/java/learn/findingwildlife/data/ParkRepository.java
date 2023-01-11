package learn.findingwildlife.data;

import learn.findingwildlife.model.Park;

import java.util.List;

public interface ParkRepository {
    List<Park> findAll();
    Park findByParkId(int parkId);

}
