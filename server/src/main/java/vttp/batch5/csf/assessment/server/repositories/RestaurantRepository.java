package vttp.batch5.csf.assessment.server.repositories;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.csf.models.Receipt;
import vttp.batch5.csf.models.User;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {
    @Autowired
    private JdbcTemplate template;

    public static final String GET_CUSTOMER_USERNAME = "SELECT * FROM customers WHERE username = ?";
    public static final String INSERT_ORDER = "INSERT INTO place_orders(order_id, payment_id, order_date, total, username) VALUES (?, ?, ?, ?, ?)";

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

    public void insertOrder(Receipt receipt, String username, double total) {
        // Task 3
        Object[] data = new Object[] {
                receipt.getOrder_id(),
                receipt.getPayment_id(),
                new Date(receipt.getTimestamp()),
                total,
                username
        };

        template.update(INSERT_ORDER, data);
    }
}
