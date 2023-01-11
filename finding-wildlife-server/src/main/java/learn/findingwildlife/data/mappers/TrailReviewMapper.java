package learn.findingwildlife.data.mappers;

import learn.findingwildlife.model.TrailReview;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class TrailReviewMapper implements RowMapper<TrailReview> {


    @Override
    public TrailReview mapRow(ResultSet rs, int i) throws SQLException {
        TrailReview trailReview = new TrailReview();
        trailReview.setTrailReviewId(rs.getInt("trail_review_id"));
        trailReview.setRating(rs.getDouble("rating"));
        trailReview.setDifficulty(rs.getDouble("difficulty"));
        trailReview.setComments(rs.getString("comments"));
        trailReview.setTrailId(rs.getInt("trail_id"));
        trailReview.setAppUserId(rs.getInt("app_user_id"));
        return trailReview;
    }
}

