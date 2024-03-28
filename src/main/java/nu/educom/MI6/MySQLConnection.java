package nu.educom.MI6;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQLConnection {
    public static Connection connect() throws SQLException {

        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Get database credentials from nu.educom.MI6.DatabaseConfig class
            var jdbcUrl = DatabaseConfig.getDbUrl();
            //System.getenv to access the local variables (don't want to put user & password in configure file
            var user = System.getenv(DatabaseConfig.getDbUsername());
            var password = System.getenv(DatabaseConfig.getDbPassword());
            // Open a connection
            return DriverManager.getConnection(jdbcUrl, user, password);

        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load JDBC driver class: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}