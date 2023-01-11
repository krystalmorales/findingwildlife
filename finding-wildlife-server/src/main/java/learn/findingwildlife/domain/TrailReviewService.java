package learn.findingwildlife.domain;

import learn.findingwildlife.data.TrailReviewRepository;
import learn.findingwildlife.model.TrailReview;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrailReviewService {
    private final TrailReviewRepository repository;

    public TrailReviewService(TrailReviewRepository repository) {
        this.repository = repository;
    }

    public List<TrailReview> findByTrailReviewId(int trailReviewId) {
        return repository.findByTrailReviewId(trailReviewId);
    }
    public List<TrailReview> findByTrailId(int trailId) {
        return repository.findByTrailId(trailId);
    }

    public List<TrailReview> findByUserId(int userId) {return repository.findByUser(userId);}

    public Result<TrailReview> createTrailReview(TrailReview trailReview) {
        Result<TrailReview> result = validate(trailReview);
        if (!result.isSuccess()) {
            return result;
        }
        if (trailReview.getTrailReviewId() != 0) {
            result.addErrorMessage("Id cannot be set for `add` operation");
            return result;
        }
        trailReview = repository.createTrailReview(trailReview);
        result.setPayload(trailReview);
        return result;
    }

    public Result<TrailReview> updateTailReview(TrailReview trailReview) {
        Result<TrailReview> result = validate(trailReview);
        if(!result.isSuccess()) {
            return result;
        }
        if(trailReview.getTrailReviewId() <= 0) {
            result.addErrorMessage("Trail review id must be set for `update` operation");
        }
        if(!repository.updateTrailReview(trailReview)) {
            String msg = String.format("Trail review id: %s, not found", trailReview.getTrailReviewId());
            result.addErrorMessage(msg);
        }
        if(!repository.updateTrailReview(trailReview)) {
            String msg = String.format("app user id: %s, not found", trailReview.getAppUserId());
            result.addErrorMessage(msg);
        }
        return result;
    }

    public Result<TrailReview> deleteById(int trailReviewId, int appUSerId) {
        Result<TrailReview> result = new Result<>();
        if(!repository.deleteTrialReview(trailReviewId, appUSerId)) {
            result.addErrorMessage(String.format("Trail review ID %d does not exist", trailReviewId));
        }
        return result;
    }

    private Result<TrailReview> validate(TrailReview trailReview) {
        Result<TrailReview> result = new Result<>();
        if(trailReview == null) {
            result.addErrorMessage("Trail Review cannot be empty!");
        }

        if (trailReview.getRating() < 1.0 || trailReview.getRating() > 5.0) {
            result.addErrorMessage("Trail rating must be between 1 and 5");
        }

        if (trailReview.getDifficulty() < 1.0 || trailReview.getDifficulty() > 5.0) {
            result.addErrorMessage("Trail difficulty must be between 1 and 5");
        }
        return result;
    }

}
