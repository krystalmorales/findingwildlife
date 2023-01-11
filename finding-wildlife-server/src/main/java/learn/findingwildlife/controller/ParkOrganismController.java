package learn.findingwildlife.controller;

import learn.findingwildlife.domain.AppUserService;
import learn.findingwildlife.domain.ParkOrganismService;

import learn.findingwildlife.domain.Result;
import learn.findingwildlife.model.AppUser;
import learn.findingwildlife.model.ParkOrganism;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhoat:3000"})
@RequestMapping(path="/finding-wildlife/park-organism")
public class ParkOrganismController {
    @Autowired
    private final ParkOrganismService service;

    @Autowired
    private final AppUserService appUserService;

    public ParkOrganismController(ParkOrganismService service, AppUserService appUserService) {
        this.service = service;
        this.appUserService = appUserService;
    }

    @GetMapping("/{parkId}")
    public ResponseEntity<?> findByParkId(@PathVariable int parkId) {
        List<ParkOrganism> parkOrganismsInPark = service.findByParkId(parkId);
        return new ResponseEntity<>(parkOrganismsInPark, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createParkOrganism(@RequestHeader(name = "authorization") AppUser user, @RequestBody ParkOrganism parkOrganism) {
        Result<?> authorized = appUserService.validateUserRoleAsAdmin(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<ParkOrganism> result = service.createParkOrganism(parkOrganism);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{parkOrganismId}")
    public ResponseEntity<?> updateParkOrganism(@RequestHeader(name = "authorization") AppUser user, @PathVariable int parkOrganismId, @RequestBody ParkOrganism parkOrganism) {
        Result<?> authorized = appUserService.validateUserRoleAsAdmin(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        if (parkOrganismId != parkOrganism.getParkOrganismId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<ParkOrganism> result = service.updateParkOrganism(parkOrganism);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }
}
