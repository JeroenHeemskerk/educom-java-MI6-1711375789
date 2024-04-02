package nu.educom.MI6;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Mi6Model implements Contact.iMi6Model {

    public boolean validateLogin(String userNum, String password) {
        boolean auth = false;
        Agent agent = SQLQuerier.getAgent(userNum);
        if (agent != null)
            auth = Objects.equals(password,agent.getPassphrase());
        return auth;
    }
    public List<LoginAttempts> fetchLogins(String userNum){
        List<LoginAttempts> failedAttempts = SQLQuerier.getLastLoginAttempts(userNum);
        return failedAttempts;
    }
    public void calculateCooldownTime() {

    }


}
