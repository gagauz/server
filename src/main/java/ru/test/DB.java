package ru.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DB {
    private static Connection connection;

    private DB() {

    }

    public static void open() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/b4f", "b4f", "office");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void close() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/b4f", "b4f", "office");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static DB instance() {
        return new DB();

    }

}
