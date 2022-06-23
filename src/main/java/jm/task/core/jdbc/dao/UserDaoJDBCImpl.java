package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final static String CREATINGSTR = """
         CREATE TABLE IF NOT EXISTS `users` (
         `ID` INT NOT NULL AUTO_INCREMENT,
         `Name` VARCHAR(30) NOT NULL,
         `LastName` VARCHAR(30) NOT NULL,
         `Age` TINYINT(3) NOT NULL,
         PRIMARY KEY (`ID`))""";
    private final static String DROPINGSTR = "DROP TABLE IF EXISTS users";
    private final static String SAVINGSTR = "INSERT INTO users (Name, Lastname, Age) VALUES (?, ?, ?)";
    private final static String REMOVINGSTR = "DELETE FROM users WHERE ID = ?";
    private final static String GETINGSTR = "SELECT * FROM users";
    private final static String CLEANINGSTR = "TRUNCATE TABLE users";
    private final Connection connection = Util.getConnect();



    public UserDaoJDBCImpl() {

    }



    public void createUsersTable() {
        try (Statement statement = connection.createStatement() ) {
            statement.executeUpdate(CREATINGSTR);
            System.out.println("Successfully creating table \"users\"");
        } catch (SQLException e) {
            System.out.println("Oops, something wrong with creating");
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROPINGSTR);
            System.out.println("Successfully drop table \"users\"");
        } catch (SQLException e) {
            System.out.println("Oops, something wrong with drop");
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVINGSTR)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("user - %s %s, was added.\n", name, lastName);
        } catch (SQLException e) {
            System.out.println("Oops, something wrong with saving");
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVINGSTR)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.printf("users with id - %d, was remove.", id);
        }catch (SQLException e) {
            System.out.println("Oops, something wrong with removing");
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GETINGSTR)) {
            while (resultSet.next()) {
                User bufferUser = new User();

                bufferUser.setName(resultSet.getString("Name"));
                bufferUser.setLastName(resultSet.getString("LastName"));
                bufferUser.setAge(resultSet.getByte("Age"));
                bufferUser.setId(resultSet.getLong("ID"));

                users.add(bufferUser);
            }
        } catch (SQLException e) {
            System.out.println("Oops, something wrong with getting");
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CLEANINGSTR);
            System.out.println("Table was cleaning");
        } catch (SQLException e) {
            System.out.println("Oops, something wrong with cleaning");
            e.printStackTrace();
        }
    }
}
