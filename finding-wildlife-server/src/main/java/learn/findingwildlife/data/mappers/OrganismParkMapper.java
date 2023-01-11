package learn.findingwildlife.data.mappers;

import learn.findingwildlife.model.Nativeness;
import learn.findingwildlife.model.OrganismPark;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganismParkMapper implements RowMapper<OrganismPark> {

    @Override
    public OrganismPark mapRow(ResultSet rs, int i) throws SQLException {
        OrganismPark organismPark = new OrganismPark();
        organismPark.setOrganismId(rs.getInt("organism_id"));
        organismPark.setAbundance(rs.getString("abundance"));
        organismPark.setNativeness(Nativeness.findByName(rs.getString("nativeness")));

        ParkMapper parkMapper = new ParkMapper();
        organismPark.setPark(parkMapper.mapRow(rs, i));

        return organismPark;
    }
}
