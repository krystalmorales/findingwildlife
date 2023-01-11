package learn.findingwildlife.domain;

import learn.findingwildlife.data.ParkRepository;
import learn.findingwildlife.model.Park;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkService {

    private final ParkRepository repository;

    public ParkService(ParkRepository repository) {
        this.repository = repository;
    }

    public List<Park> findAll() {
        return repository.findAll();
    }

    public Park findByParkId(int parkId) {
        return repository.findByParkId(parkId);
    }

}
