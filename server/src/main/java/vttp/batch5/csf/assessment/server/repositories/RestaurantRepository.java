package vttp.batch5.csf.assessment.server.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.csf.models.User;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {
    @Autowired
    private JdbcTemplate template;

    public static final String GET_CUSTOMER_USERNAME = "SELECT * FROM customers WHERE username = ?";

    public Optional<User> getUserByUsername(String username) throws SQLException {
        try {
            User user = template.queryForObject(GET_CUSTOMER_USERNAME, 
                        BeanPropertyRowMapper.newInstance(User.class), username);
            
            return Optional.of(user);
        }
        catch(DataAccessException ex) {
            return Optional.empty();
        }
    }
}
