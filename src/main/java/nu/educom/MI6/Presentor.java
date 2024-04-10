package nu.educom.MI6;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
@Controller
public class Presentor {
    private static Mi6Model model;
    private static JFrameView view;
    public static void main(String[] args) {
        SpringApplication.run(Presentor.class, args);
    }

    public Presentor(Mi6Model model, JFrameView view) {
        this.model = model;
        this.view = view;
        this.view.RegisterPresentorListener(this);
        System.setProperty("java.awt.headless", "false");
        view.JFrameView();
    }

    public static void handleCommand(String command) {
        if (Objects.equals(command, "login")){
           handleLogin();
        }
    }

    public static void handleLogin(){
        // Input validation happens here in the Presentor
        String text = "";
        String serviceNumber = view.serviceNumberInput.getText();
        String password = view.passwordInput.getText();
        serviceNumber = bond(serviceNumber);
        List<String> serviceNumbers = serviceNumbers();
        if (!serviceNumbers.contains(serviceNumber)){
            view.showMessage("This is not a service number");

        } else {
            boolean auth = model.validateLogin(serviceNumber, password);
            List<LoginAttempts> failedAttempts = model.fetchLogins(serviceNumber);
            int cooldown = model.calculateCooldownTime(failedAttempts);
            if (cooldown > 0){
                text = "You have to wait " + cooldown + " minutes";
                auth = false;
            } else {
                model.uploadLoginAttempt(serviceNumber, auth);
            }
            if (auth){
                text = model.generateLoginMessage(serviceNumber, failedAttempts);
            }
        }
        view.showMessage(text);
    }

    public static List<String> serviceNumbers(){
        List<String> numbers = new ArrayList<>();
        for (int i = 1; i <= 999; i++) {
            numbers.add(String.format("%03d", i));
        }
        return numbers;
    }

    public static String bond(String userNum){
        if (Objects.equals(userNum, "7") || Objects.equals(userNum, "07")){
            userNum = "007";
        }
        return userNum;
    }
}