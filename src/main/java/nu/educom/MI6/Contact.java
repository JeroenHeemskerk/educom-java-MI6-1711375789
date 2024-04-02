package nu.educom.MI6;

import java.util.List;

public interface Contact {
    interface iJFrameView {
        void showMessage(String message);
        void RegisterPresentorListener(Presentor presentor);
    }

    interface iMi6Model {
        boolean validateLogin(String userNum, String password);
        List<LoginAttempts> fetchLogins(String userNum);
        void calculateCooldownTime();
    }

    interface iPresentor {
        void handleCommand(String command);
    }
}
