package nu.educom.MI6;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SQLQuerier {
    public static void getAll() {
        String sql = "SELECT * " +
                "FROM agent";
        try (var conn = MySQLConnection.connect();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(
                        rs.getString("servicenumber")
                );

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static Agent getAgent(String serviceNumber) {
        Agent agent = null;
        String sql = "SELECT * " +
                "FROM agent " +
                "WHERE servicenumber = ?;";
        try (var conn = MySQLConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, serviceNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String servNumber = rs.getString("servicenumber");
                    String passphrase = rs.getString("passphrase");
                    String active = rs.getString("Active");
                    String licenced = rs.getString("licenced_to_kill");
                    String expiration = rs.getString("licence_expiration");

                    agent = new Agent(id, servNumber, passphrase, active, licenced, expiration);
                }
            }
            return agent;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static List<LoginAttempts> getLastLoginAttempts(String serviceNumber) {
        List<LoginAttempts> loginAttemptsList = new ArrayList<>();
        //The LEFT JOIN is based on the most recent successful login
        //This allows the login_time to be by comparing this most recent successful login
        //And it works even if there is no recent successful login because then MAX is null
        String sql = "SELECT * FROM login_attempts " +
                "WHERE login_time >= (" +
                "SELECT MAX(login_time)" +
                "FROM login_attempts " +
                "WHERE login_success = true " +
                "AND servicenumber = ?) " +
                "AND login_success = false " +
                "AND servicenumber = ? " +
                "ORDER BY login_time DESC;";
        try (var conn = MySQLConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, serviceNumber);
            pstmt.setString(2, serviceNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String servNumber = rs.getString("servicenumber");
                    Timestamp loginTime = rs.getTimestamp("login_time");
                    boolean loginSuccess = rs.getBoolean("login_success");

                    LoginAttempts loginAttempt = new LoginAttempts(servNumber, loginTime, loginSuccess);
                    loginAttemptsList.add(loginAttempt);
                }
            }
            return loginAttemptsList;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return loginAttemptsList;
    }

    public static void loginAttemptUpdate(String serviceNumber, boolean success) {
        String sql = "INSERT INTO login_attempts (servicenumber, login_success) " +
                "VALUES (?, ?)";
        try (var conn = MySQLConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, serviceNumber);
            pstmt.setBoolean(2, success);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
