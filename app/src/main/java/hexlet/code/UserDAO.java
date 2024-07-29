package hexlet.code;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection conn) {
        this.connection = conn;
    }

    public void save(User user) throws SQLException {
        if (user.getId() == null) {
            var sql = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.executeUpdate();
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }
            }
        } else {
            var sql = "UPDATE users SET ? = ?";
            try (var preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "username");
                preparedStatement.setString(2, user.getUsername());
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "phone");
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.executeUpdate();
            }
        }
    }

    public Optional<User> find(Long id) throws SQLException {
        var sql = "SELECT * FROM users WHERE id = ?";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var userName = resultSet.getString("username");
                var phone = resultSet.getString("phone");
                User user = new User(userName, phone);
                user.setId(id);
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }

    public void delete(User user) throws SQLException {
        var sql5 = "DELETE FROM users WHERE username = ?";

        try (var preparedStatement = connection.prepareStatement(sql5)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.executeUpdate();
        }
    }
    public List<User> getEntities() throws SQLException {
        List<User> users = new ArrayList<>();
        var sql = "SELECT * FROM users";
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String userName = resultSet.getString("username");
                String phone = resultSet.getString("phone");
                User user = new User(userName, phone);
                user.setId(id);
                users.add(user);
            }
        }
        return users;
    }
}
