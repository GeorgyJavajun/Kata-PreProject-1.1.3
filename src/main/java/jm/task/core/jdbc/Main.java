package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;


public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoJDBCImpl();

        userDao.createUsersTable();
        userDao.saveUser("Ronnie", "Radke", (byte) 38);
        userDao.saveUser("Denny", "Worsnop", (byte) 31);
        userDao.saveUser("Serj", "Tankian", (byte) 45);
        userDao.saveUser("Mitch", "Lucker", (byte) 28);
        userDao.removeUserById(1);
        userDao.getAllUsers().forEach(System.out::println);
        userDao.cleanUsersTable();
        userDao.dropUsersTable();



    }
}
