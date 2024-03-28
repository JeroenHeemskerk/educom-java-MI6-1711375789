package nu.educom.MI6;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SQLQuerier {
    public static void getAll(){
        String sql = "SELECT * " +
                "FROM agent";
        try (var conn = MySQLConnection.connect();
             var stmt  = conn.createStatement();
             var rs    = stmt.executeQuery(sql)) {

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

    public static List<LoginAttempts> getLastLoginAttempts(String serviceNumber){
        List<LoginAttempts> loginAttemptsList = new ArrayList<>();
        //The LEFT JOIN is based on the most recent successful login
        //This allows the login_time to be by comparing this most recent successful login
        //And it works even if there is no recent successful login because then MAX is null
        String sql = "SELECT * FROM login_attempts " +
                "WHERE login_time >= (" +
                    "SELECT IFNULL(MAX(login_time), DATE_SUB(NOW(), INTERVAL 2 MINUTE)) " +
                    "FROM login_attempts " +
                    "WHERE login_success = true " +
                    "AND servicenumber = 'your_servicenumber') " +
                "AND login_success = false " +
                "AND servicenumber = 'your_servicenumber' " +
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

                    LoginAttempts loginAttempt = new LoginAttempts(id, servNumber, loginTime, loginSuccess);
                    loginAttemptsList.add(loginAttempt);
                }
            }
            return loginAttemptsList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return loginAttemptsList;
    }

    public static void loginAttemptUpdate(String serviceNumber, boolean success){
        String sql = "INSERT INTO login_attempts (servicenumber, login_success) " +
                "VALUES (?, ?)";
        try (var conn = MySQLConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, serviceNumber);
            pstmt.setBoolean(2, success);
            pstmt.executeUpdate();
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static boolean authenticateAgent(String serviceNumber, String secret){
        // I want to try make the password check an innate part of the query, to minimize access to it
        boolean auth = false;
        String sql = "SELECT servicenumber, licenced_to_kill, licence_expiration " +
                "FROM agent " +
                "WHERE servicenumber = ? " +
                "AND passphrase = ? " +
                "AND Active = 'yes' ";
        try (var conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, serviceNumber);
            pstmt.setString(2, secret);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    auth = true;
                }
            }
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return auth;
    }
}
