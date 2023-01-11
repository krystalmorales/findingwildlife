package learn.findingwildlife.controller;

import learn.findingwildlife.domain.AppUserService;
import learn.findingwildlife.domain.Result;
import learn.findingwildlife.domain.SightingsService;
import learn.findingwildlife.model.AppUser;
import learn.findingwildlife.model.Sighting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/finding-wildlife/sighting")
public class SightingController {

    @Autowired
    private final SightingsService service;

    @Autowired
    private final AppUserService appUserService;

    public SightingController(SightingsService service, AppUserService appUserService) {
        this.service = service;
        this.appUserService = appUserService;
    }

    @GetMapping("/date/{dateTo}/{date}/{parkId}")
    public List<Sighting> findSightingByDate(@PathVariable("dateTo") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo, @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,  @PathVariable("parkId") int parkId) {

        return service.findSightingByDate(dateTo, date, parkId);
    }

    @GetMapping("/national-park/{nationalPark}")
    public ResponseEntity<?> findNationalPark(@PathVariable String nationalPark) {
        List<Sighting> sightings = service.findByNationalPark(nationalPark);
        if(sightings == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sightings);
    }

    @GetMapping("/{organismCommonName}")
    public ResponseEntity<?> findOrganismCommonName(@PathVariable String organismCommonName) {
        List<Sighting> sightings = service.findByOrganism(organismCommonName);
        if(sightings == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sightings);
    }

    @GetMapping("/sightingId/{sightingId}")
    public ResponseEntity<?> findBySightingId(@PathVariable int sightingId) {
        List<Sighting> sightings = service.findBySightingId(sightingId);
        if(sightings == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sightings);
    }

    @GetMapping("/userId/{userId}")
    public List<Sighting> findByUserId(@RequestHeader(name = "authorization") AppUser user, @PathVariable int userId) {

        return service.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<?> createSighting(@RequestHeader(name = "authorization") AppUser user, @RequestBody Sighting sighting) {
        Result<?> authorized = appUserService.validateUser(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<?> admin = appUserService.validateUser(user);

        if (!admin.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<Sighting> result = service.createSighting(sighting);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{sightingId}")
    public ResponseEntity<Object> update(@RequestHeader(name = "authorization") AppUser user, @PathVariable int sightingId, @RequestBody Sighting sighting) {
        Result<?> authorized = appUserService.validateUser(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<?> admin = appUserService.validateUser(user);

        if (!admin.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }
        if(user.getAppUserId() != sighting.getAppUserId()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);

        }

        Result<Sighting> result = service.updateSighting(sighting);
        if(!result.isSuccess()) {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{sightingId}/{appUserId}")
    public ResponseEntity<?> deleteById(@RequestHeader(name = "authorization") AppUser user, @PathVariable("sightingId") int sightingId, @PathVariable("appUserId") int appUserId) {
        Result<?> authorized = appUserService.validateUser(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<?> admin = appUserService.validateUser(user);

        if (!admin.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<Sighting> result = service.deleteById(sightingId, appUserId);

        if(!result.isSuccess()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

