package learn.findingwildlife.model;

public class TrailReview {
    private int trailReviewId;
    private int trailId;
    private double rating;
    private double difficulty;
    private String comments;
    private int appUserId;

    public TrailReview() {
    }

    public TrailReview(int trailReviewId, int trailId, double rating, double difficulty, String comments) {
        this.trailReviewId = trailReviewId;
        this.trailId = trailId;
        this.rating = rating;
        this.difficulty = difficulty;
        this.comments = comments;
    }

    public TrailReview(int trailReviewId, int trailId, double rating, double difficulty, String comments, int appUserId) {
        this.trailReviewId = trailReviewId;
        this.trailId = trailId;
        this.rating = rating;
        this.difficulty = difficulty;
        this.comments = comments;
        this.appUserId = appUserId;
    }

    public int getTrailReviewId() {
        return trailReviewId;
    }

    public void setTrailReviewId(int trailReviewId) {
        this.trailReviewId = trailReviewId;
    }

    public int getTrailId() {
        return trailId;
    }

    public void setTrailId(int trailId) {
        this.trailId = trailId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }
}
