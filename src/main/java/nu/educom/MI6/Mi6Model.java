package nu.educom.MI6;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class Mi6Model implements Contact.iMi6Model {

    private final HQLQuerier hqlQuerier;

    @Autowired
    public Mi6Model(HQLQuerier hqlQuerier) {
        this.hqlQuerier = hqlQuerier;
    }

    @Override
    public boolean validateLogin(String userNum, String password) {
        boolean auth = false;
        Agent agent = hqlQuerier.readAgent(userNum);
        if (agent != null)
            auth = (BCrypt.checkpw(password, agent.getPassphrase()));
        return auth;
    }
    @Override
    public List<LoginAttempts> fetchLogins(String userNum){
        List<LoginAttempts> failedAttempts = hqlQuerier.readLastLoginAttempts(userNum);
        return failedAttempts;
    }

    public void uploadLoginAttempt(String userNum, boolean auth){
        hqlQuerier.updateLoginAttempt(userNum, auth);
    }
    @Override
    public int calculateCooldownTime(List<LoginAttempts> failedAttempts) {
        // You only have to calculate cooldown time if a failed attempt was found
        int cooldown = 0;
        if (!failedAttempts.isEmpty()) {
            cooldown = 1;
            Timestamp loginTime = failedAttempts.getFirst().getLoginTime();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime specificDate = loginTime.toLocalDateTime();
            Duration duration = Duration.between(specificDate, now);
            int minutesSinceLastFail = (int) duration.toMinutes();
            for (LoginAttempts ignored : failedAttempts){
                cooldown = cooldown * 2;
            }
            cooldown = cooldown - minutesSinceLastFail;
        }
        return cooldown;
    }

    public String generateLoginMessage(String serviceNumber, List<LoginAttempts> failedAttempts) {
        StringBuilder messageLogin = new StringBuilder();
        Agent license = SQLQuerier.getAgent(serviceNumber);
        String licenseActive = license.getLicensed();
        String licenseExpiration = license.getExpiration();
        messageLogin.append("Licensed: ").append(licenseActive)
                .append("Expires on:").append(licenseExpiration);

        for (LoginAttempts attempt : failedAttempts) {
            // Access the details of each failed login attempt
            int id = attempt.getId();
            String servNumber = attempt.getServiceNumber();
            Timestamp loginTimeM = attempt.getLoginTime();
            boolean loginSuccess = attempt.isLoginSuccess();

            // Append the details to the StringBuilder
            messageLogin.append("ID: ").append(id)
                    .append(", Service Number: ").append(servNumber)
                    .append(", Login Time: ").append(loginTimeM)
                    .append(", Success: ").append(loginSuccess)
                    .append(System.lineSeparator()); // Add newline
        }
        return messageLogin.toString();
    }
}
