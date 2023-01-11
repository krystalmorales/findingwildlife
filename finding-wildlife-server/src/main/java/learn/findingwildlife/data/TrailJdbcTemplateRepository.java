package learn.findingwildlife.data;

import learn.findingwildlife.data.mappers.TrailMapper;
import learn.findingwildlife.model.Trail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TrailJdbcTemplateRepository implements TrailRepository {
    private final JdbcTemplate template;
    public TrailJdbcTemplateRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Trail> findAll() {
        final String sql = "select trail_id, trail_name, trail_number, trail_distance, park_id " +
                "from trail;";

        List<Trail> trail = template.query(sql, new TrailMapper()).stream().collect(Collectors.toList());

        return trail;
    }

}
