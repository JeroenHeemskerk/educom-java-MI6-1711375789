package nu.educom.MI6;
import org.mindrot.jbcrypt.BCrypt;

public class Main {
  public static void main(String[] args) {
    JFrameView view = new JFrameView();
    Mi6Model model = new Mi6Model();
    Presentor presentor = new Presentor(model, view);

  }
}