package learn.findingwildlife.data;

import learn.findingwildlife.data.mappers.TrailReviewMapper;
import learn.findingwildlife.model.TrailReview;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
@Profile("jdbcRepo")
public class TrailReviewJdbcTemplateRepository implements TrailReviewRepository {
    private final JdbcTemplate template;

    public TrailReviewJdbcTemplateRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<TrailReview> findByTrailReviewId(int trailReviewId) {
        final String sql = "select r.trail_review_id, r.rating, r.difficulty, r.comments, r.trail_id, r.app_user_id "
                + "from trail_review r "
                + "inner join trail tr on r.trail_id = tr.trail_id "
                + "where r.trail_review_id = ?;";

        return template.query(sql, new TrailReviewMapper(), trailReviewId);
    }

    @Override
    public List<TrailReview> findByTrailId(int trailId) {
        final String sql = "select r.trail_review_id, r.rating, r.difficulty, r.comments, r.trail_id, r.app_user_id "
                + "from trail_review r "
                + "inner join trail tr on r.trail_id = tr.trail_id "
                + "where r.trail_id = ?;";

        return template.query(sql, new TrailReviewMapper(), trailId);
    }

    @Override
    public List<TrailReview> findByUser(int userId) {
        final String sql = "select r.trail_review_id, r.rating, r.difficulty, r.comments, r.trail_id, r.app_user_id "
                + "from trail_review r "
                + "inner join trail tr on r.trail_id = tr.trail_id "
                + "where r.app_user_id = ?;";

        return template.query(sql, new TrailReviewMapper(), userId);
    }

    @Override
    public TrailReview createTrailReview(TrailReview trailReview) {

        final String sql = "insert into trail_review (rating, difficulty, comments, trail_id, app_user_id) "
                + "values (?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, trailReview.getRating());
            ps.setDouble(2, trailReview.getDifficulty());
            ps.setString(3, trailReview.getComments());
            ps.setInt(4, trailReview.getTrailId());
            ps.setInt(5, trailReview.getAppUserId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }
        trailReview.setTrailReviewId(keyHolder.getKey().intValue());

        return trailReview;
    }

    @Override
    public boolean updateTrailReview(TrailReview trailReview) {

        final String sql = "update trail_review set "
                + "rating = ?, "
                + "difficulty = ?, "
                + "comments = ? "
                + "where trail_review_id = ?" +
                " and app_user_id = ?;";


        return template.update(sql,
                trailReview.getRating(),
                trailReview.getDifficulty(),
                trailReview.getComments(),
                trailReview.getTrailReviewId(),
                trailReview.getAppUserId()) > 0;

    }

    @Override
    @Transactional
    public boolean deleteTrialReview(int trailReviewId, int appUserID) {
        return template.update("delete from trail_review where trail_review_id = ? and app_user_id = ?;", trailReviewId, appUserID) > 0;
    }
}
