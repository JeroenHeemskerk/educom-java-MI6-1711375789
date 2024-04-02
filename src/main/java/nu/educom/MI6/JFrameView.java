package nu.educom.MI6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JFrameView {
    private ActionListener loginButton = new LoginAction();
    static JTextField serviceNumberInput;
    static JTextField passwordInput;
    static JLabel message;
    private Presentor presentor;

    public void RegisterPresentorListener(Presentor presentor) {
        this.presentor = presentor;
    }

    public void showMessage(String messageText){
        message.setText(messageText);
    }

    public void JFrameView() {
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
    private class LoginAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Presentor.handleCommand( e.getActionCommand());
        }
    }
}
