package nu.educom.MI6;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "login_attempts")
public class LoginAttempts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "servicenumber")
    private String serviceNumber;
    @Column(name = "login_time")
    private Timestamp loginTime;
    @Column(name = "login_success")
    private boolean loginSuccess;

    public LoginAttempts(){}

    public LoginAttempts(String serviceNumber, Timestamp loginTime, boolean loginSuccess) {
        this.serviceNumber = serviceNumber;
        this.loginTime = loginTime;
        this.loginSuccess = loginSuccess;
    }


    public int getId() {
        return id;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }
}
