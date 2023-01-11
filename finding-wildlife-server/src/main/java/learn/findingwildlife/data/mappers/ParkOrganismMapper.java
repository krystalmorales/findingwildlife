package learn.findingwildlife.data.mappers;

import learn.findingwildlife.model.Nativeness;
import learn.findingwildlife.model.ParkOrganism;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkOrganismMapper implements RowMapper<ParkOrganism> {
    @Override
    public ParkOrganism mapRow(ResultSet rs, int i) throws SQLException {
        ParkOrganism parkOrganism = new ParkOrganism();
        parkOrganism.setParkOrganismId(rs.getInt("park_organism_id"));

        parkOrganism.setParkId(rs.getInt("park_id"));
        parkOrganism.setAbundance(rs.getString("abundance"));
        parkOrganism.setNativeness(Nativeness.findByName(rs.getString("nativeness")));

        OrganismMapper organismMapper = new OrganismMapper();
        parkOrganism.setOrganism(organismMapper.mapRow(rs, i));

        return parkOrganism;
    }
}