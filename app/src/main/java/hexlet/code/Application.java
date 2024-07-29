package hexlet.code;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Application {
    private UserDAO userDAO;
    public static void main(String[] args) throws SQLException {

        try (var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test")) {
            var sql = "CREATE TABLE users (id LONG PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone INT)";

            try (var statement = conn.createStatement()) {
                statement.execute(sql);
            }

            UserDAO dao = new UserDAO(conn);

            User user = new User("Tommy", "123456789");
            User user2 = new User("Denis", "232424");

            dao.save(user);
            dao.save(user2);
            System.out.println(dao.getEntities());

            System.out.println(dao.find(2L).get());

            dao.delete(user2);
            System.out.println(dao.find(2L));
        }
    }
}
