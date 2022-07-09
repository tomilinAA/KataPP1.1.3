package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        String request = "CREATE TABLE IF NOT EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.execute(request + "(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "name varchar(100), " +
                    "lastName varchar(100), " +
                    "age tinyint)");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void dropUsersTable() {
        String request = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            statement.execute(request);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String FormatRequest = "INSERT INTO users (name, lastName, age) VALUES ('%s', '%s', (%d))";
        String request = String.format(FormatRequest, name, lastName, age);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(request);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void removeUserById(long id) {
        String request = "DELETE FROM users WHERE id = " + id + " LIMIT 1 ";
        try (Statement statement = connection.createStatement()) {
            statement.execute(request);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String request = "SELECT * FROM users";
        try (ResultSet resultSet = connection.createStatement().executeQuery(request)) {
            while (resultSet.next()) {
                User user = new User(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return userList;
    }

    public void cleanUsersTable() {
        String request = "TRUNCATE TABLE users";
        try (Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE users");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
