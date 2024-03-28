package nu.educom.MI6;

import java.sql.Timestamp;

public class LoginAttempts {
    private int id;
    private String serviceNumber;
    private Timestamp loginTime;
    private boolean loginSuccess;

    public LoginAttempts(int id, String serviceNumber, Timestamp loginTime, boolean loginSuccess) {
        this.id = id;
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
