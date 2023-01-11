package learn.findingwildlife.data.mappers;

import learn.findingwildlife.model.Trail;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrailMapper implements RowMapper<Trail> {

    @Override
    public Trail mapRow(ResultSet rs, int i) throws SQLException {
        Trail trail = new Trail();
        trail.setTrailId(rs.getInt("trail_id"));
        trail.setTrailName(rs.getString("trail_name"));
        trail.setTrailNumber(rs.getInt("trail_number"));
        trail.setDistance(rs.getDouble("trail_distance"));
        trail.setParkId(rs.getInt("park_id"));

        return trail;
    }
}
