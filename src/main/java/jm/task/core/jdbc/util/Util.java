package jm.task.core.jdbc.util;
import java.sql.*;
public class Util {
    private static final String USER = "root";
    private static final String PASSWORD = "password278";
    private static final String URL = "jdbc:mysql://localhost:3306/mysql";

    public static Connection getConnect() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection success");
        } catch (SQLException | ClassNotFoundException  e) {
            System.out.println("Connection fail");
        }
        return connection;
    }
}
