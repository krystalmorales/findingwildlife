package learn.findingwildlife.data;


import learn.findingwildlife.data.mappers.ParkOrganismMapper;
import learn.findingwildlife.model.ParkOrganism;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ParkOrganismJdbcTemplateRepository implements ParkOrganismRepository {
    private final JdbcTemplate template;

    public ParkOrganismJdbcTemplateRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    @Transactional
    public List<ParkOrganism> findByParkId(int parkId) {
        final String sql = "select po.park_organism_id, po.park_id, po.abundance, po.nativeness, po.organism_id, "
                + "o.common_name, o.scientific_name, o.category "
                + "from park_organism po "
                + "inner join organism o on po.organism_id = o.organism_id "
                + "where po.park_id = ?;";

        return template.query(sql, new ParkOrganismMapper(), parkId)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public ParkOrganism createParkOrganism(ParkOrganism parkOrganism) {

        final String sql = "insert into park_organism (abundance, nativeness, park_id, organism_id)" +
                "values (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowCount = template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, parkOrganism.getAbundance());
            ps.setString(2, parkOrganism.getNativeness().name());
            ps.setInt(3, parkOrganism.getParkId());
            ps.setInt(4, parkOrganism.getOrganism().getOrganismId());

            return ps;
        }, keyHolder);

        if (rowCount == 0) {
            return null;
        }

        parkOrganism.setParkOrganismId(keyHolder.getKey().intValue());

        return parkOrganism;
    }

    @Override
    public boolean updateParkOrganism(ParkOrganism parkOrganism) {

        final String sql = "update park_organism set "
                + "abundance = ?, "
                + "nativeness = ? "
                + "where park_id = ? and organism_id = ?;";

        return template.update(sql,
                parkOrganism.getAbundance(),
                parkOrganism.getNativeness().getName(),
                parkOrganism.getParkId(),
                parkOrganism.getOrganism().getOrganismId()) > 0;
    }
}
