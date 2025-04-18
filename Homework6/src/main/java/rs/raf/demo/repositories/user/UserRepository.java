package rs.raf.demo.repositories.user;

import rs.raf.demo.entities.User;
import rs.raf.demo.repositories.MySqlAbstractRepository;


import java.sql.*;

public class UserRepository extends MySqlAbstractRepository implements UserInterface{

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = newConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = newConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(resultSet.getString("password"));
                    return user;
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by username", e);
        }
    }
}
