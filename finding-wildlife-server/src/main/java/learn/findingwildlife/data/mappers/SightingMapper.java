package learn.findingwildlife.data.mappers;

import learn.findingwildlife.model.Sighting;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class SightingMapper implements RowMapper<Sighting> {

    @Override
    public Sighting mapRow(ResultSet resultSet, int i) throws SQLException {
        Sighting sighting = new Sighting();
        sighting.setSightingId(resultSet.getInt("sighting_id"));
        sighting.setDate(resultSet.getDate("date").toLocalDate());
        sighting.setTime(resultSet.getTime("time").toLocalTime());
        sighting.setComments(resultSet.getString("comments"));
        sighting.setLatitude(resultSet.getDouble("latitude"));
        sighting.setLongitude(resultSet.getDouble("longitude"));
        sighting.setOrganismId(resultSet.getInt("organism_id"));
        sighting.setParkId(resultSet.getInt("park_id"));
        sighting.setAppUserId(resultSet.getInt("app_user_id"));


        ParkMapper parkMapper = new ParkMapper();
        sighting.setPark(parkMapper.mapRow(resultSet, i));

        OrganismMapper organismMapper = new OrganismMapper();
        sighting.setOrganism(organismMapper.mapRow(resultSet, i));

        return sighting;
    }
}


