package org.library.db;

import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static String getDatabasePath() {
        URL resourceUrl = DatabaseConnection.class.getClassLoader().getResource("database/library.db");
        if (resourceUrl == null) {
            throw new RuntimeException("Database file not found in resources!");
        }
        return "jdbc:sqlite:" + resourceUrl.getPath();
    }

    private static final String url = getDatabasePath();


    private DatabaseConnection() {

        try {

            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);

        } catch (SQLException e) {

            throw new RuntimeException("Failed to connect to database.", e);

        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                instance = null;
            }
        } catch (SQLException e) {
            System.err.println("Error closing SQLite connection: " + e.getMessage());
        }
    }
}
