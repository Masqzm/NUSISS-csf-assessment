package vttp.batch5.csf.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String username;
    private String password;

    public static User populate(ResultSet rs) throws SQLException {
        User user = new User();

        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));

        return user;
    }
    
    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + "]";
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
