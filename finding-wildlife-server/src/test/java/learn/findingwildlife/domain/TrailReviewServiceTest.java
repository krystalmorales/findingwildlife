package learn.findingwildlife.domain;

import learn.findingwildlife.data.TrailReviewRepository;
import learn.findingwildlife.model.TrailReview;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TrailReviewServiceTest {

    @Autowired
    TrailReviewService service;

    @MockBean
    TrailReviewRepository repository;

    private void setupTrailId() {
        Mockito
                .when(repository.findByTrailId(1))
                .thenReturn(Arrays.asList(
                        new TrailReview(1, 1, 5.5, 3.3, "Fun for experienced hikers"),
                        new TrailReview(2, 2, 5, 3.2, "Best trail EVER!!!!")
                ));
    }
    private void setupTrailReviewId() {
        Mockito
                .when(repository.findByTrailReviewId(1))
                .thenReturn(Arrays.asList(
                        new TrailReview(1, 1, 5.5, 3.3, "Fun for experienced hikers"),
                        new TrailReview(2, 2, 5, 3.2, "Best trail EVER!!!!")
                ));
    }

    private void setupUserId() {
        Mockito
                .when(repository.findByUser(1))
                .thenReturn(Arrays.asList(
                        new TrailReview(1, 1, 5.5, 3.3, "Fun for experienced hikers", 1),
                        new TrailReview(2, 2, 5, 3.2, "Best trail EVER!!!!", 1)
                ));
    }
    @Test
    void shouldFindByTrailReviewId1(){
        setupTrailReviewId();

        List<TrailReview> trailReviewList = service.findByTrailReviewId(1);
        assertNotNull(trailReviewList);
        assertEquals(2, trailReviewList.size());
    }

    @Test
    void shouldFindTrailId1() {
        setupTrailId();

        List<TrailReview> trailReviewList = service.findByTrailId(1);
        assertNotNull(trailReviewList);
        assertEquals(2, trailReviewList.size());

    }

    @Test
    void shouldFindUserId1() {
        setupUserId();

        List<TrailReview> userList = service.findByUserId(1);
        assertNotNull(userList);
        assertEquals(2, userList.size());
    }

    @Test
    void shouldNotAddTrailReview() {
        TrailReview trailReview = makeTrailReview();
        Result<TrailReview> result = service.createTrailReview(trailReview);
        assertTrue(result.getErrorMessages().contains("Id cannot be set for `add` operation"));

        trailReview.setTrailReviewId(0);
        trailReview.setRating(-1);
        result = service.createTrailReview(trailReview);
        assertTrue(result.getErrorMessages().contains("Trail rating must be between 1 and 5"));

        trailReview.setTrailReviewId(0);
        trailReview.setRating(2);
        trailReview.setDifficulty(6);
        result = service.createTrailReview(trailReview);
        assertTrue(result.getErrorMessages().contains("Trail difficulty must be between 1 and 5"));
    }

    @Test
    void shouldAdd(){
        TrailReview trailReview = new TrailReview();
        trailReview.setRating(1.0);
        trailReview.setDifficulty(2.0);
        trailReview.setComments(null);
        trailReview.setTrailId(2);

        TrailReview trailReviewToAdd = makeTrailReview();

        when(repository.createTrailReview(trailReview)).thenReturn(trailReviewToAdd);

        Result<TrailReview> actual = service.createTrailReview(trailReview);
        assertEquals(trailReviewToAdd, actual.getPayload());
    }

    @Test
    void shouldNotUpdateNullTrailReview() {
        TrailReview trailReview = makeTrailReview();
        trailReview.setTrailReviewId(0);

        when(repository.updateTrailReview(trailReview)).thenReturn(true);
        Result<TrailReview> result = service.updateTailReview(trailReview);
        assertTrue(result.getErrorMessages().contains("sighting id must be set for `update` operation"));
    }

    @Test
    void shouldNotUpdateNullFields() {

        TrailReview trailReview = makeTrailReview();
        trailReview.setRating(-1);
        when(repository.updateTrailReview(trailReview)).thenReturn(true);
        Result<TrailReview> result = service.updateTailReview(trailReview);
        assertTrue(result.getErrorMessages().contains("Trail rating must be between 1 and 5"));

        trailReview.setRating(1);
        trailReview.setDifficulty(6);
        result = service.updateTailReview(trailReview);
        assertTrue(result.getErrorMessages().contains("Trail difficulty must be between 1 and 5"));
    }

    @Test
    void shouldUpdate() {
        TrailReview trailReview = makeTrailReview();
        trailReview.setRating(3.0);
        trailReview.setDifficulty(4.0);
        trailReview.setComments("");

        when(repository.updateTrailReview(trailReview)).thenReturn(true);
        Result<TrailReview> result = service.updateTailReview(trailReview);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldDelete() {
        when(repository.deleteTrialReview(1, 1)).thenReturn(true);
        assertTrue(service.deleteById(1, 1).isSuccess());
    }

    private TrailReview makeTrailReview() {
        TrailReview trailReview = new TrailReview();
        trailReview.setTrailReviewId(5);
        trailReview.setRating(2.0);
        trailReview.setDifficulty(5.0);
        trailReview.setComments("Trail is fantastic!");
        trailReview.setTrailId(1);
        trailReview.setAppUserId(1);

        return trailReview;
    }
}