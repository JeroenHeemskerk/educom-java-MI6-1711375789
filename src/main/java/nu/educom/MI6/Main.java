package nu.educom.MI6;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class Main {
  public static void main(String[] args) {
    List<String> blackList = new ArrayList<>();
    while (blackList.size() != 956){
      String userNum = userNumber(blackList);
      boolean login = userLogin();
      if (login) {
        System.out.println("Welcome " + userNum);
      } else {
        blackList.add(userNum);
      }
    }
  }

  public static boolean userLogin(){
    boolean login = false;
    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
    System.out.println("Enter code");
    String code = myObj.nextLine();
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

  public static String userNumber(List<String> blackList) {
    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
    String userNum = "";
    //Need to make an array that contains 001 to 956
    List<String> numbers = new ArrayList<>();
    for (int i = 1; i <= 956; i++) {
      numbers.add(String.format("%03d", i));
    }

    while (!numbers.contains(userNum) || blackList.contains(userNum)) {
      System.out.println("Enter service number:");
      userNum = myObj.nextLine();
      userNum = bond(userNum);
    }

    return userNum;  // Return the username
  }



}