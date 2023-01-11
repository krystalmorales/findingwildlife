package learn.findingwildlife.controller;

import learn.findingwildlife.domain.AppUserService;
import learn.findingwildlife.domain.Result;
import learn.findingwildlife.domain.TrailReviewService;
import learn.findingwildlife.model.AppUser;
import learn.findingwildlife.model.Trail;
import learn.findingwildlife.model.TrailReview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/finding-wildlife/trail-review")
public class TrailReviewController {
    @Autowired
    private final TrailReviewService service;
    @Autowired
    private final AppUserService appUserService;

    public TrailReviewController(TrailReviewService service, AppUserService appUserService) {
        this.service = service;
        this.appUserService = appUserService;
    }

    @GetMapping("/trail-review-id/{trailReviewId}")
    public List<TrailReview> findByTrailReviewId(@PathVariable int trailReviewId) {
        return service.findByTrailReviewId(trailReviewId);
    }

    @GetMapping("/trailId/{trailId}")
    public List<TrailReview> findTrailId(@PathVariable int trailId) throws DataAccessException {
        return service.findByTrailId(trailId);
    }

    @GetMapping("/userId/{userId}")
    public List<TrailReview> findByUserId(@RequestHeader(name = "authorization") AppUser user, @PathVariable int userId) {

        return service.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<?> createTrailReview(@RequestHeader(name = "authorization") AppUser user, @RequestBody TrailReview trailReview) {
        Result<?> authorized = appUserService.validateUser(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<TrailReview> result = service.createTrailReview(trailReview);

        if(result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{trailReviewId}")
    public ResponseEntity<Object> update(@RequestHeader(name = "authorization") AppUser user, @PathVariable int trailReviewId, @RequestBody TrailReview trailReview) {
        Result<?> authorized = appUserService.validateUser(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<?> admin = appUserService.validateUser(user);

        if (!admin.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }
        if(user.getAppUserId() != trailReview.getAppUserId()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<TrailReview> result = service.updateTailReview(trailReview);
        if(!result.isSuccess()) {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{trailReviewId}/{appUserId}")
    public ResponseEntity<?> deleteById(@RequestHeader(name = "authorization") AppUser user, @PathVariable("trailReviewId") int trailReviewId, @PathVariable("appUserId") int appUserId) {
        Result<?> authorized = appUserService.validateUser(user);

        if (!authorized.isSuccess()) {
            return new ResponseEntity<>(authorized.getErrorMessages(), HttpStatus.FORBIDDEN);
        }

        Result<TrailReview> result = service.deleteById(trailReviewId, appUserId);

        if(!result.isSuccess()) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
