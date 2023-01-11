package learn.findingwildlife.controller;

import learn.findingwildlife.domain.AppUserService;
import learn.findingwildlife.domain.OrganismService;
import learn.findingwildlife.domain.Result;
import learn.findingwildlife.model.AppUser;
import learn.findingwildlife.model.Organism;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhoat:3000"})
@RequestMapping("/finding-wildlife/organism")
public class OrganismController {

    @Autowired
    private final OrganismService service;
    @Autowired
    private final AppUserService appUserService;

    public OrganismController(OrganismService service, AppUserService appUserService) {
        this.service = service;
        this.appUserService = appUserService;
    }

    @GetMapping("/{scientificName}")
    public ResponseEntity<?> findByScientificName(@PathVariable String scientificName) {
        Result<Organism> result = service.findByScientificName(scientificName);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }

        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<?> createOrganism(@RequestHeader(name = "authorization") AppUser user, @RequestBody Organism organism) {
        Result<?> authorized = appUserService.validateUserRoleAsAdmin(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<Organism> result = service.createOrganism(organism);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{organismId}")
    public ResponseEntity<?> updateOrganism(@RequestHeader(name = "authorization") AppUser user, @PathVariable int organismId, @RequestBody Organism organism) {
        Result<?> authorized = appUserService.validateUserRoleAsAdmin(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        if (organismId != organism.getOrganismId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Organism> result = service.updateOrganism(organism);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{organismId}")
    public ResponseEntity<?> deleteById(@RequestHeader(name = "authorization") AppUser user, @PathVariable int organismId) {
        Result<?> authorized = appUserService.validateUserRoleAsAdmin(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<Organism> result = service.deleteById(organismId);
        if(result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }
}
