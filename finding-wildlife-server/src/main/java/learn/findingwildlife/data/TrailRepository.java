package learn.findingwildlife.data;

import learn.findingwildlife.model.Trail;

import java.util.List;

public interface TrailRepository {
    List<Trail> findAll();
}
