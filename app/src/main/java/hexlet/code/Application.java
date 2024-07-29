package hexlet.code;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    public static void main(String[] args) throws SQLException {

        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone INT)";

            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            var sql2 = "INSERT INTO users (username, phone) VALUES ('tommy', '123456789')";
            try (var statement2 = conn.createStatement()) {
                statement2.executeUpdate(sql2);
            }

            var sql3 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql3);

                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    System.out.println(resultSet.getString("phone"));
                }
            }

            var sql4 = "INSERT INTO users (username, phone) VALUES (?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql4, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, "Denis");
                preparedStatement.setString(2, "33333333");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Maria");
                preparedStatement.setString(2, "44444444");
                preparedStatement.executeUpdate();

                var generatedKey = preparedStatement.getGeneratedKeys();
                if (generatedKey.next()) {
                    System.out.println(generatedKey.getLong(1));
                } else {
                    throw new SQLException("DB have not returned an id after saving the entity");
                }
            }

            var sql5 = "DELETE FROM users WHERE username = ?";

            try (var preparedStatement = conn.prepareStatement(sql5)) {
                preparedStatement.setString(1, "Maria");
                preparedStatement.executeUpdate();
            }

            var sql6 = "SELECT * FROM users";
            try (var statement3 = conn.createStatement()) {
                var resultSet = statement3.executeQuery(sql6);

                while (resultSet.next()) {
                    System.out.printf("%d %s %s \n", resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("phone"));

                }
            }
        }
    }
}
