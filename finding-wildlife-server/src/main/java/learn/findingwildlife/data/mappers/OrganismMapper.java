package learn.findingwildlife.data.mappers;

import learn.findingwildlife.model.Organism;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganismMapper implements RowMapper<Organism>{
    @Override
    public Organism mapRow(ResultSet rs, int i) throws SQLException {
        Organism organism = new Organism();
        organism.setOrganismId(rs.getInt("organism_id"));
        organism.setScientificName(rs.getString("scientific_name"));
        organism.setCommonName(rs.getString("common_name"));
        organism.setCategory(rs.getString("category"));
        return organism;
    }
}
