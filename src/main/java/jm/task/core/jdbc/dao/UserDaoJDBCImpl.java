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

    public void createUsersTable() throws SQLException {
        String request = "CREATE TABLE IF NOT EXISTS users";
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute(request + "(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                    "name varchar(100), " +
                    "lastName varchar(100), " +
                    "age tinyint)");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void dropUsersTable() throws SQLException {
        String request = "DROP TABLE IF EXISTS users";
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute(request);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        String FormatRequest = "INSERT INTO users (name, lastName, age) VALUES ('%s', '%s', (%d))";
        String request = String.format(FormatRequest, name, lastName, age);
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate(request);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void removeUserById(long id) throws SQLException {
        String request = "DELETE FROM users WHERE id = " + id + " LIMIT 1 ";
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute(request);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<User> getAllUsers() throws SQLException {
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
        } finally {
            connection.setAutoCommit(true);
        }
        return userList;
    }

    public void cleanUsersTable() throws SQLException {
        String request = "TRUNCATE TABLE users";
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute(request);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
