package cn.edu.jmu.chengyi.superlibrary.ui;

import java.awt.Font;

import java.awt.BorderLayout;
import javax.swing.*;

import cn.edu.jmu.chengyi.superlibrary.User;

public class WindowsControl {
    public static User user;

    public static void main(String[] args) {
        Login();
        System.out.println("username");
    }

    private static void FindFail(JFrame up) {
        JFrame frame = new JFrame("查无此书");
        frame.setSize(210, 160);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JButton yes = new JButton("查无此书");
        yes.setHorizontalAlignment(SwingConstants.CENTER);
        yes.setFont(new Font("宋体", Font.PLAIN, 23));
        yes.setBounds(0, 0, 200, 150);
        panel.add(yes);

        yes.addActionListener(arg -> {
            up.setEnabled(true);
            frame.dispose();
        });

        frame.setVisible(true);

    }

    private static void Find() {
        JFrame frame = new JFrame("查询界面");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JLabel BookName = new JLabel("书名:");
        BookName.setBounds(10, 20, 80, 25);
        BookName.setFont(new Font("宋体", Font.PLAIN, 23));
        panel.add(BookName);

        JTextField BookNameText = new JTextField(20);
        BookNameText.setBounds(70, 20, 165, 25);
        panel.add(BookNameText);

        JLabel author = new JLabel("作者:");
        author.setBounds(10, 50, 80, 25);
        author.setFont(new Font("宋体", Font.PLAIN, 23));
        panel.add(author);

        JTextField authorText = new JTextField(20);
        authorText.setBounds(70, 50, 165, 25);
        panel.add(authorText);

        JButton lkfButton = new JButton("查询");
        lkfButton.setBounds(70, 120, 80, 25);
        panel.add(lkfButton);

        lkfButton.addActionListener(arg -> {
            FindFail(frame);
            // frame.dispose();
            frame.setEnabled(false);
        });

        frame.setVisible(true);
    }

    private static void Login() {
        JFrame frame = new JFrame("登录界面");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JLabel userLabel = new JLabel("账号:");
        userLabel.setFont(new Font("宋体", Font.PLAIN, 23));
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(70, 20, 165, 25);
        panel.add(userText);

        JLabel password = new JLabel("密码:");
        password.setFont(new Font("宋体", Font.PLAIN, 23));
        password.setBounds(10, 50, 80, 25);
        panel.add(password);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(70, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(70, 80, 80, 25);
        panel.add(loginButton);

        JButton RegisterButton = new JButton("Register");
        RegisterButton.setBounds(70, 120, 80, 25);
        panel.add(RegisterButton);

        loginButton.addActionListener(args -> {
            String s = String.valueOf(passwordText.getPassword());
            System.out.println("账号：" + userText.getText() + "   密码：" + s);
            Find();
            frame.dispose();
        });
        RegisterButton.addActionListener(args -> {
            String s = String.valueOf(passwordText.getPassword());
            System.out.println("账号：" + userText.getText() + "   密码：" + s);
            // frame.dispose();
        });
        frame.setVisible(true);
    }
    // placeComponents

}