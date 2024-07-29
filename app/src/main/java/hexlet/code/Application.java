package hexlet.code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Application {
    private static Connection conn;
    public static void main(String[] args) throws SQLException {

        conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test");
        var sql = "CREATE TABLE users (id LONG PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone INT)";

        try (var statement = conn.createStatement()) {
            statement.execute(sql);
        }

        UserDAO dao = new UserDAO(conn);

        User user = new User("Tommy", "123456789");
        User user2 = new User("Denis", "232424");

        dao.save(user);
        dao.save(user2);

        System.out.println(dao.find(2L).get());

        dao.remove(user2);
    }
}
