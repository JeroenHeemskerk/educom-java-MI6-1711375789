package nu.educom.MI6;
import java.sql.SQLException;

import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
  static List<String> blackList = new ArrayList<>();
  private static ActionListener loginButton = new LoginAction();
  static JTextField serviceNumberInput;
  static JTextField passwordInput;

  static JLabel message;

  public static void main(String[] args) {
    //SQLQuerier.getAll();
    JFrame frame = new JFrame("MI6");
    JPanel panel = new JPanel();

    BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
    panel.setLayout(boxlayout);
    //panel.setBorder(new EmptyBorder(new Insets(45, 70, 45, 70)));
    JLabel serviceNumber = new JLabel("Enter service number");
    serviceNumberInput = new JTextField(5);
    JLabel password = new JLabel("Enter password");
    passwordInput = new JTextField(5);
    JButton login = new JButton("login");
    message = new JLabel(" ");

    login.addActionListener(loginButton);

    panel.add(serviceNumber);
    panel.add(serviceNumberInput);
    panel.add(password);
    panel.add(passwordInput);
    panel.add(login);
    panel.add(message);
    frame.setLayout(new GridLayout(2, 1));
    frame.add(panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(350, 300);
    frame.setVisible(true);
  }

  private static class LoginAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      //String userNum = serviceNumberInput.getText();
      String userNum = serviceNumberInput.getText();
      String passphrase = passwordInput.getText();
      String input = e.getActionCommand();
      boolean auth = SQLQuerier.authenticateAgent(userNum,passphrase);
      long minutesRemaining = 0;
      // Whether it fails or not, we should check if its a viable number for the login tracker
      List<String> numbers = new ArrayList<>();
      for (int i = 1; i <= 999; i++) {
        numbers.add(String.format("%03d", i));
      }
      if (numbers.contains(userNum)) {

        StringBuilder failedAttemptsMessage = new StringBuilder();
        List<LoginAttempts> failedAttempts = SQLQuerier.getLastLoginAttempts(userNum);
        //And then I need all recent login fails this is for either:
        // 1) A successful login, and to display all failed logins
        // 2) A failed login, and to calculate wait time
        // And to check if the cooldown is still ongoing, which means: time check
        // Now we want to calculate a few things
        // How much cooldown time is remaining, and whether or not the failed attempt should be documented
        if (!failedAttempts.isEmpty()) {
          Timestamp loginTime = failedAttempts.get(0).getLoginTime();
          LocalDateTime now = LocalDateTime.now();
          LocalDateTime specificDate = loginTime.toLocalDateTime();
          Duration duration = Duration.between(specificDate, now);
          long minutesSinceLastFail = duration.toMinutes();
          // With minutesSinceLastFail, we can calculate wait time (note for later, if there is no failedAttempts prior, minutes since lastFail can default to 1
          // Since you only come here if you had at least one prior failed attempt
          long cooldown = 1;

          for (LoginAttempts attempt : failedAttempts) {
            // Access the details of each failed login attempt
            int id = attempt.getId();
            String servNumber = attempt.getServiceNumber();
            Timestamp loginTimeM = attempt.getLoginTime();
            boolean loginSuccess = attempt.isLoginSuccess();

            // Append the details to the StringBuilder
            failedAttemptsMessage.append("ID: ").append(id)
                    .append(", Service Number: ").append(servNumber)
                    .append(", Login Time: ").append(loginTimeM)
                    .append(", Success: ").append(loginSuccess)
                    .append(System.lineSeparator()); // Add newline

            cooldown = cooldown * 2;
          }
          minutesRemaining = cooldown - minutesSinceLastFail;
          }
        if (minutesRemaining > 0){
          auth = false;
        } else {
          SQLQuerier.loginAttemptUpdate(userNum, auth);
        }
        if (auth){
          message.setText(failedAttemptsMessage.toString());
          System.out.println(failedAttemptsMessage);
          SQLQuerier.loginAttemptUpdate(userNum, auth);
        } else {
          message.setText("Login failed, please wait " + minutesRemaining + " minutes to try again");
        }

        }
      }
  }

  public static boolean userLogin(){
    boolean login = false;
    String code = passwordInput.getText();
    if (Objects.equals(code, "For ThE Royal QUEEN")){
      login = true;
    }
    return login;
  }

  public static String bond(String userNum){
    if (Objects.equals(userNum, "7") || Objects.equals(userNum, "07")){
      userNum = "007";
    }
    return userNum;
  }

  public static String userNumber(String userNum) {
    //String userNum = serviceNumberInput.getText();
    //String userNum = "";
    //Need to make an array that contains 001 to 956
    List<String> numbers = new ArrayList<>();
    for (int i = 1; i <= 956; i++) {
      numbers.add(String.format("%03d", i));
    }
    if (!numbers.contains(userNum) || blackList.contains(userNum)) {
      userNum = bond(userNum);
    }
    return userNum;  // Return the username
  }



}