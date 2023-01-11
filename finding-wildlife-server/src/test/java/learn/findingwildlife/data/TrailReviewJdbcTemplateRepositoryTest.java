package learn.findingwildlife.data;

import learn.findingwildlife.model.TrailReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TrailReviewJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 4;

    @Autowired
    TrailReviewJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindByTrailReviewId() {
        List<TrailReview> actual = repository.findByTrailReviewId(1);
        assertNotNull(actual);
    }

    @Test
    void shouldFindId1() {
        List<TrailReview> actual = repository.findByTrailId(1);
        assertNotNull(actual);

    }

    @Test
    void shouldUserId() {
        List<TrailReview> actual = repository.findByUser(1);
        assertNotNull(actual);
    }

    @Test
    void shouldAdd() {
        TrailReview trailReview = makeTrailReview();
        TrailReview actual = repository.createTrailReview(trailReview);
        assertNotNull(actual);
        assertEquals(NEXT_ID, actual.getTrailReviewId());

    }

    @Test
    void shouldUpdate() {
        TrailReview trailReview = updatedTrailReview();
        trailReview.setTrailReviewId(1);
        trailReview.setAppUserId(1);
        assertTrue(repository.updateTrailReview(trailReview));
        assertEquals(1, trailReview.getRating());
    }

    @Test
    void shouldNotDeleteIfNonExistent() {
        assertFalse(repository.deleteTrialReview(20, 3));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteTrialReview(1, 1));
    }

    private TrailReview updatedTrailReview() {
        TrailReview trailReview = new TrailReview();
        trailReview.setRating(1);
        trailReview.setDifficulty(1);
        trailReview.setComments("Great trail!!");
        return trailReview;
    }

    private TrailReview makeTrailReview() {
        TrailReview trailReview = new TrailReview();
        trailReview.setRating(1);
        trailReview.setDifficulty(1);
        trailReview.setComments("Great trail!!");
        trailReview.setTrailId(2);
        return trailReview;

    }
}