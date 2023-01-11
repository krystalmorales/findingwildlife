package learn.findingwildlife.data;

import learn.findingwildlife.data.mappers.ParkMapper;
import learn.findingwildlife.data.mappers.TrailMapper;
import learn.findingwildlife.model.Park;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ParkJdbcTemplateRepository implements ParkRepository {

    private final JdbcTemplate template;

    public ParkJdbcTemplateRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Park> findAll() {
        final String sql = "select park_id, park_name, center_longitude, center_latitude from park;";
        return template.query(sql, new ParkMapper());
    }

    @Override
    @Transactional
    public Park findByParkId(int parkId) {
        final String sql = "select park_id, park_name, center_longitude, center_latitude "
                + "from park "
                + "where park_id = ?;";

        Park park = template.query(sql, new ParkMapper(), parkId)
                .stream()
                .findAny()
                .orElse(null);

        if (park != null) {
            addTrails(park);
        }

        return park;
    }

    private void addTrails(Park park) {
        final String sql = "select trail_id, trail_name, trail_number, trail_distance, park_id "
                + "from trail "
                + "where park_id = ?;";

        var trails = template.query(sql, new TrailMapper(), park.getParkId());
        park.setTrails(trails);
    }
}
