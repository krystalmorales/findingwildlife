package learn.findingwildlife.data.mappers;

import learn.findingwildlife.model.Park;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkMapper implements RowMapper<Park> {

    @Override

    public Park mapRow(ResultSet rs,int i) throws SQLException {
        Park park = new Park();
        park.setParkId(rs.getInt("park_id"));
        park.setParkName(rs.getString("park_name"));
        park.setCenterLatitude(rs.getDouble("center_latitude"));
        park.setCenterLongitude(rs.getDouble("center_longitude"));
        return park;
    }

}
