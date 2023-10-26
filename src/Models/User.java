package Models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int user_id;
    private long social_security_number;
    private String name;
    private String email;
    private String phone;
    private String password;
    private int online;
    private LocalDateTime created;

    public User(){

    }

    public static List<User> getAllUsers(Connection connection) throws SQLException {
        List<User> userList = new ArrayList<>();

        String query = "SELECT * FROM users";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            User user = new User();
            user.setUser_id(resultSet.getInt("user_id"));
            user.setSocial_security_number(resultSet.getLong("social_security_number"));
            user.setName(resultSet.getString("name"));
            user.setEmail(resultSet.getString("email"));
            user.setPhone(resultSet.getString("phone"));
            user.setPassword(resultSet.getString("password"));

            userList.add(user);
        }

        return userList;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getSocial_security_number() {
        return social_security_number;
    }

    public void setSocial_security_number(long social_security_number) {
        this.social_security_number = social_security_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
