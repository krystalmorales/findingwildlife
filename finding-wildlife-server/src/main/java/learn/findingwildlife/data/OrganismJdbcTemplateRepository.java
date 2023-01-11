package learn.findingwildlife.data;


import learn.findingwildlife.data.mappers.OrganismMapper;
import learn.findingwildlife.data.mappers.OrganismParkMapper;
import learn.findingwildlife.model.Organism;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class OrganismJdbcTemplateRepository implements OrganismRepository {
    private final JdbcTemplate template;

    public OrganismJdbcTemplateRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Organism findByScientificName(String scientificName) {
        String lowerCaseScientificName = scientificName.toLowerCase();

        final String sql = "select organism_id, common_name, scientific_name, category "
                + "from organism "
                + "where scientific_name = ?;";

        Organism organism = template.query(sql, new OrganismMapper(), lowerCaseScientificName).stream()
                .findFirst().orElse(null);

        if (organism != null) {
            addParks(organism);
        }

        return organism;

    }

    @Override
    public Organism createOrganism(Organism organism) {

        final String sql = "insert into organism (common_name, scientific_name, category)" +
                "values (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowCount = template.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, organism.getCommonName());
            preparedStatement.setString(2, organism.getScientificName().toLowerCase());
            preparedStatement.setString(3, organism.getCategory());

            return preparedStatement;
        }, keyHolder);

        if (rowCount == 0) {
            return null;
        }

        organism.setOrganismId(keyHolder.getKey().intValue());

        return organism;

    }

    @Override
    public boolean updateOrganism(Organism organism) {

        final String sql = "update organism set " +
                "common_name = ?, " +
                "scientific_name = ?, " +
                "category = ?" +
                "where organism_id = ?;";

        return template.update(sql,
                organism.getCommonName(),
                organism.getScientificName(),
                organism.getCategory(),
                organism.getOrganismId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteOrganism(int organismId) {
        return template.update("delete from organism where organism_id = ?;", organismId) > 0;
    }

    @Override
    public boolean findAnyReference(int organismId) {

        final String sql = "select count(*) > 0 from park_organism where organism_id = ?;";

        return template.queryForObject(sql, Boolean.class, organismId);
    }

    private void addParks(Organism organism) {
        final String sql = "select po.park_id, po.organism_id, po.abundance, po.nativeness, "
                + "p.park_name, p.center_latitude, p.center_longitude "
                + "from park_organism po "
                + "inner join park p on po.park_id = p.park_id "
                + "where po.organism_id = ?;";

        var organismParks = template.query(sql, new OrganismParkMapper(), organism.getOrganismId());
        organism.setParks(organismParks);
    }
}
