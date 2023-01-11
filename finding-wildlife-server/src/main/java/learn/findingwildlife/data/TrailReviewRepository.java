package learn.findingwildlife.data;

import learn.findingwildlife.model.TrailReview;

import java.util.List;

public interface TrailReviewRepository {
    List<TrailReview> findByTrailReviewId(int trailReviewId);
    List<TrailReview> findByTrailId(int trailId);
    List<TrailReview> findByUser(int userId);
    TrailReview createTrailReview(TrailReview trailReview);
    boolean updateTrailReview(TrailReview trailReview);
    boolean deleteTrialReview(int trailReviewId, int appUserId);
}
