package learn.findingwildlife.controller;

import learn.findingwildlife.domain.ParkService;
import learn.findingwildlife.model.Park;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/finding-wildlife/park")
public class ParkController {

    @Autowired
    private final ParkService service;

    public ParkController(ParkService service) {
        this.service = service;
    }

    @GetMapping
    public List<Park> findAll() {
        return service.findAll();
    }

    @GetMapping("/{parkId}")
    public Park findByParkId(@PathVariable int parkId) {
        return service.findByParkId(parkId);
    }
}
