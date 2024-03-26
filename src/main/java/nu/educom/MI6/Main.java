package nu.educom.MI6;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
      String userNum = serviceNumberInput.getText();
      String input = e.getActionCommand();
      userNum = userNumber(userNum);
      boolean login = userLogin();
      if (login) {
        message.setText("Login successful, welcome " + userNum);
      } else {
        message.setText("ACCESS DENIED");
        blackList.add(userNum);
      }
      System.out.println("Test");
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