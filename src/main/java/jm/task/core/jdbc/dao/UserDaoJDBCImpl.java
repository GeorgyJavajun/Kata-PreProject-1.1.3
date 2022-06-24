package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
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
        Savepoint savePoint = null;

        try (Statement statement = connection.createStatement() ) {
            savePoint = connection.setSavepoint();
            statement.executeUpdate(CREATINGSTR);
            System.out.println("Successfully creating table \"users\"");
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Oops, something wrong with creating");
            e.printStackTrace();
            try {
                connection.rollback(savePoint);
            } catch (SQLException ex) {
                System.out.println("Trouble with rollback");
                ex.printStackTrace();
            }
        }
    }

    public void dropUsersTable() {
        Savepoint savePoint = null;

        try (Statement statement = connection.createStatement()) {
            savePoint = connection.setSavepoint();
            statement.executeUpdate(DROPINGSTR);
            System.out.println("Successfully drop table \"users\"");
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Oops, something wrong with drop");
            e.printStackTrace();
            try {
                connection.rollback(savePoint);
            } catch (SQLException ex) {
                System.out.println("Trouble with rollback ");
                ex.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Savepoint savePoint = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVINGSTR)) {
            savePoint = connection.setSavepoint();
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("user - %s %s, was added.\n", name, lastName);
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Oops, something wrong with saving");
            e.printStackTrace();
            try {
                connection.rollback(savePoint);
            } catch (SQLException ex) {
                System.out.println("Trouble with rollback");
                ex.printStackTrace();
            }
        }
    }

    public void removeUserById(long id) {
        Savepoint savePoint = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVINGSTR)) {
            savePoint = connection.setSavepoint();
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.printf("users with id - %d, was remove.", id);
            connection.commit();
        }catch (SQLException e) {
            System.out.println("Oops, something wrong with removing");
            e.printStackTrace();
            try {
                connection.rollback(savePoint);
            } catch (SQLException ex) {
                System.out.println("Trouble with rollback");
                ex.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Savepoint savePoint = null;

        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GETINGSTR)) {
            savePoint = connection.setSavepoint();

            while (resultSet.next()) {
                User bufferUser = new User();

                bufferUser.setName(resultSet.getString("Name"));
                bufferUser.setLastName(resultSet.getString("LastName"));
                bufferUser.setAge(resultSet.getByte("Age"));
                bufferUser.setId(resultSet.getLong("ID"));

                users.add(bufferUser);
            }
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Oops, something wrong with getting");
            e.printStackTrace();
            try {
                connection.rollback(savePoint);
            } catch (SQLException ex) {
                System.out.println("Trouble with rollback");
                ex.printStackTrace();
            }
        }
        return users;
    }

    public void cleanUsersTable() {
        Savepoint savePoint = null;

        try (Statement statement = connection.createStatement()) {
            savePoint = connection.setSavepoint();
            statement.execute(CLEANINGSTR);
            System.out.println("Table was cleaning");
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Oops, something wrong with cleaning");
            e.printStackTrace();
            try {
                connection.rollback(savePoint);
            } catch (SQLException ex) {
                System.out.println("Trouble with rollback");
                ex.printStackTrace();
            }
        }
    }
}
