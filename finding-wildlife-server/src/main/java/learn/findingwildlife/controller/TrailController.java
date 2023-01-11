package learn.findingwildlife.controller;

import learn.findingwildlife.domain.AppUserService;
import learn.findingwildlife.domain.TrailService;
import learn.findingwildlife.model.Trail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/finding-wildlife/trail")
public class TrailController {

    @Autowired
    private final TrailService service;

    @Autowired
    private final AppUserService appUserService;

    public TrailController(TrailService service, AppUserService appUserService) {
        this.service = service;
        this.appUserService = appUserService;
    }

    @GetMapping
    public List<Trail> findAll() {
        return service.findAll();
    }
}
