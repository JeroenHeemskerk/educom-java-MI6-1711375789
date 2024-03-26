package nu.educom.MI6;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
  public static void main(String[] args) {
    List<String> blackList = new ArrayList<>();
    JFrame frame = new JFrame("InputDialog Example #1");
    while (blackList.size() != 956){
      String userNum = userNumber(blackList, frame);
      boolean login = userLogin(frame);
      if (login) {
        JOptionPane.showMessageDialog(frame, "Welcome " + userNum);
      } else {
        JOptionPane.showMessageDialog(frame, "ACCESS DENIED", "ACCESS DENIED", JOptionPane.WARNING_MESSAGE);
        blackList.add(userNum);
      }
    }
  }

  public static boolean userLogin(JFrame frame){
    boolean login = false;
    String code = JOptionPane.showInputDialog(frame, "Enter passphrase");
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

  public static String userNumber(List<String> blackList, JFrame frame) {
    String userNum = "";
    //Need to make an array that contains 001 to 956
    List<String> numbers = new ArrayList<>();
    for (int i = 1; i <= 956; i++) {
      numbers.add(String.format("%03d", i));
    }

    while (!numbers.contains(userNum) || blackList.contains(userNum)) {
      userNum = JOptionPane.showInputDialog(frame, "Enter servicenumber");
      userNum = bond(userNum);
    }

    return userNum;  // Return the username
  }



}