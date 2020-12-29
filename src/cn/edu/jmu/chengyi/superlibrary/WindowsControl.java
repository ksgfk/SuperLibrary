package cn.edu.jmu.chengyi.superlibrary;

import java.awt.event.ActionEvent;
// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JLabel;
// import javax.swing.JPanel;
// import javax.swing.JPasswordField;
// import javax.swing.JTextField;
import java.awt.event.ActionListener;
import javax.swing.*;

class login implements ActionListener {

    public JPasswordField pas;
    public JTextField user;
    public JFrame frame;

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = String.valueOf(pas.getPassword());

        // @@GetPassword
        // @@GetUsername

        System.out.println("账号：" + user.getText() + "   密码：" + s);
        WindowsControl.username = user.getText();
        frame.dispose();
    }
}

class register implements ActionListener {

    public JPasswordField pas;
    public JTextField user;
    public JFrame frame;

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = String.valueOf(pas.getPassword());

        // @@GetPassword
        // @@GetUsername

        System.out.println("账号：" + user.getText() + "   密码：" + s);
        WindowsControl.username = user.getText();
        frame.dispose();
    }
}

public class WindowsControl {
    public static String username;

    public static void main(String[] args) {
        Login();
        System.out.println("username");
    }

    public static void Login() {
        JFrame frame = new JFrame("Login Example");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        frame.add(panel);
        panel.setLayout(null);

        login p = new login();
        register reg = new register();

        JLabel userLabel = new JLabel("账号:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(70, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(70, 50, 165, 25);
        panel.add(passwordText);
        System.out.println(passwordText.toString());

        JButton loginButton = new JButton("login");
        loginButton.setBounds(70, 80, 80, 25);
        panel.add(loginButton);

        JButton RegisterButton = new JButton("Register");
        RegisterButton.setBounds(70, 120, 80, 25);
        panel.add(RegisterButton);

        p.frame = frame;
        p.user = userText;
        p.pas = passwordText;

        reg.frame = frame;
        reg.user = userText;
        reg.pas = passwordText;

        loginButton.addActionListener(p);
        RegisterButton.addActionListener(reg);
        frame.setVisible(true);
    }
    // placeComponents

}