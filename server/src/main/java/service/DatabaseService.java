package service;

import java.sql.*;


public class DatabaseService {

    private static Connection connection;

    static {
        try {
            String url = "jdbc:mysql:5051/chess_db";
            String username = "root";
            String password = "password";
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Database connection failed");
            throw new RuntimeException(e);
        }
    }

    public static void clearDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM users; DELETE FROM games; DELETE FROM auth_tokens;");
        }
    }

    public static void registerUser(String username, String password, String email) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, email);
            statement.executeUpdate();
        }
    }

    public static boolean authenticateUser(String username, String password) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT password FROM users WHERE username = ?"
        )) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return storedPassword.equals(password);
            }
        }
        return false;
    }
}
