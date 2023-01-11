package learn.findingwildlife.domain;

import learn.findingwildlife.data.TrailRepository;
import learn.findingwildlife.model.Trail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrailService {

    private final TrailRepository repository;

    public TrailService(TrailRepository repository) {
        this.repository = repository;
    }

    public List<Trail> findAll(){return repository.findAll();}
}
