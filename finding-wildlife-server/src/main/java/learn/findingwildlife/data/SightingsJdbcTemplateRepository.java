
package learn.findingwildlife.data;

import learn.findingwildlife.data.mappers.OrganismMapper;
import learn.findingwildlife.data.mappers.ParkMapper;
import learn.findingwildlife.data.mappers.SightingMapper;
import learn.findingwildlife.model.Organism;
import learn.findingwildlife.model.Park;
import learn.findingwildlife.model.Sighting;

import org.springframework.context.annotation.Profile;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("jdbcRepo")

public class SightingsJdbcTemplateRepository implements SightingsRepository {

    private final JdbcTemplate jdbcTemplate;

    public SightingsJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Sighting> findSightingByDate(LocalDate date, LocalDate dateTwo, int parkId) {
        final String sql = "select s.sighting_id, s.date, s.time, s.comments, s.latitude, s.longitude, s.park_id, s.organism_id, s.app_user_id, " +
                "p.park_name, p.center_latitude, p.center_longitude, " +
                "o.common_name, o.scientific_name, o.category "
                + "from sighting s "
                + "inner join park p on s.park_id = p.park_id "
                + "inner join organism o on s.organism_id = o.organism_id "
                + "where s.date between ? and ?" +
                "and s.park_id = ?;";
        return jdbcTemplate.query(sql, new SightingMapper(), date, dateTwo,  parkId);
    }

    @Override
    public List<Sighting> findByNationalPark(String parkName) {
        final String sql = "select s.sighting_id, s.date, s.time, s.comments, s.latitude, s.longitude, s.park_id, s.organism_id, s.app_user_id, " +
                "p.park_name, p.center_latitude, p.center_longitude, " +
                "o.common_name, o.scientific_name, o.category "
                + "from sighting s "
                + "inner join park p on s.park_id = p.park_id "
                + "inner join organism o on s.organism_id = o.organism_id "
                + "where p.park_name = ?;";
        List<Sighting> sighting = jdbcTemplate.query(sql, new SightingMapper(), parkName).stream().collect(Collectors.toList());

        if (sighting != null ) {
            addPark(sighting);
            addOrganism(sighting);
        }
        return sighting;
    }

    private void addPark(List<Sighting> sightings) {
        final String sql = "select park_id, park_name, center_latitude, center_longitude " +
                "from park " +
                "where park_id = ?;";
        for(Sighting sighting : sightings) {
            Park park = jdbcTemplate.query(sql, new ParkMapper(), sighting.getParkId()).stream().findAny().orElse(null);
            sighting.setPark(park);
        }
    }

    private void addOrganism(List<Sighting> sightings) {
        final String sql = "select organism_id, common_name, scientific_name, category " +
                "from organism " +
                "where organism_id = ?;";
        for (Sighting sighting : sightings) {
            Organism organism = jdbcTemplate.query(sql, new OrganismMapper(), sighting.getOrganismId()).stream().findAny().orElse(null);
            sighting.setOrganism(organism);
        }
    }

    public List<Sighting> findBySightingId(int sightingId) {
        final String sql ="select s.sighting_id, s.date, s.time, s.comments, s.latitude, s.longitude, s.park_id, s.organism_id, s.app_user_id, " +
                "p.park_name, p.center_latitude, p.center_longitude, " +
                "o.common_name, o.scientific_name, o.category "
                + "from sighting s "
                + "inner join park p on s.park_id = p.park_id "
                + "inner join organism o on s.organism_id = o.organism_id "
                + "where s.sighting_id = ?;";
        return jdbcTemplate.query(sql, new SightingMapper(), sightingId);
    }

    @Override
    public List<Sighting> findByOrganism(String organismCommonName)  {
        final String sql =  "select s.sighting_id, s.date, s.time, s.comments, s.latitude, s.longitude, s.park_id, s.organism_id, s.app_user_id, " +
                "p.park_name, p.center_latitude, p.center_longitude, " +
                "o.common_name, o.scientific_name, o.category "
                + "from sighting s "
                + "inner join park p on s.park_id = p.park_id "
                + "inner join organism o on s.organism_id = o.organism_id "
                + "where o.common_name = ?;";
        return jdbcTemplate.query(sql, new SightingMapper(), organismCommonName);

    }

    @Override
    public List<Sighting> findByUserId(int userId) {
        final String sql =  "select s.sighting_id, s.date, s.time, s.comments, s.latitude, s.longitude, s.park_id, s.organism_id, s.app_user_id, " +
                "p.park_name, p.center_latitude, p.center_longitude, " +
                "o.common_name, o.scientific_name, o.category "
                + "from sighting s "
                + "inner join park p on s.park_id = p.park_id "
                + "inner join organism o on s.organism_id = o.organism_id "
                + "where s.app_user_id = ?;";

        return jdbcTemplate.query(sql, new SightingMapper(), userId);
    }

    @Override
    @Transactional
    public Sighting createSighting(Sighting sighting) {
        final String sql = "insert into sighting (`date`, `time`, comments, latitude, longitude, park_id, organism_id, app_user_id) "
                + " values (?, ?, ?, ?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(sighting.getDate()));
            ps.setTime(2, Time.valueOf(sighting.getTime()));
            ps.setString(3, sighting.getComments());
            ps.setDouble(4, sighting.getLatitude());
            ps.setDouble(5, sighting.getLongitude());
            ps.setInt(6, sighting.getParkId());
            ps.setInt(7, sighting.getOrganismId());
            ps.setInt(8, sighting.getAppUserId());
            return ps;
        }, keyHolder);

        if (rowsAffected <=0) {
            return null;
        }
        sighting.setSightingId(keyHolder.getKey().intValue());
        return sighting;

     }

    @Override
    public boolean updateSighting(Sighting sighting) {
        final String sql = "update sighting set "
                + "`date` = ?, "
                + "`time` = ?, "
                + "comments = ?, "
                + "latitude = ?, "
                + "longitude = ? "
                + "where sighting_id = ?" +
                " and app_user_id = ?;";


        return jdbcTemplate.update(sql,
                sighting.getDate(),
                sighting.getTime(),
                sighting.getComments(),
                sighting.getLatitude(),
                sighting.getLongitude(),
                sighting.getSightingId(),
                sighting.getAppUserId()) > 0;

    }

    @Override
    @Transactional
    public boolean deleteSighting(int sightingId, int appUserId) {
        return jdbcTemplate.update("delete from sighting where sighting_id = ? and app_user_id = ?;", sightingId, appUserId) > 0;

    }
}