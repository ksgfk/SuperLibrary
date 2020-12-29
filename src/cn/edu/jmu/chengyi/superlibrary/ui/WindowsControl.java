package cn.edu.jmu.chengyi.superlibrary.ui;

import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import cn.edu.jmu.chengyi.superlibrary.Book;
import cn.edu.jmu.chengyi.superlibrary.DbManager;
import cn.edu.jmu.chengyi.superlibrary.User;

class ShowBook {
    Book bk;

    ShowBook(Book b) {
        bk = b;
    }

    public String toString() {
        return bk.getName();
    }
}

class LLst extends AbstractListModel<ShowBook> {
    private static final long serialVersionUID = 1L;
    int size;

    LLst() {
    }

    public int getSize() {
        return WindowsControl.BookList.size();
    }

    public ShowBook getElementAt(int index) {
        return new ShowBook(WindowsControl.BookList.get(index));
    }
}

public class WindowsControl {
    public static User user;
    public static List<Book> BookList;
    static List<Book> books = new ArrayList<Book>();
    // private JPanel contentPane;

    public static void main(String[] args) {
        Login();
        // ConsoleControl frame = new ConsoleControl();
    }

    private static void ListBooks() {
        // long serialVersionUID = 1L;J
        JFrame frame = new JFrame();
        Font nf = new Font("宋体", Font.PLAIN, 13);
        BookList = new ArrayList<>();
        BookList.add(Book.NoonBook);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 600, 502);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));

        frame.add(contentPane);

        JList<ShowBook> list = new JList<ShowBook>();
        list.setFont(nf);
        list.setModel(new LLst());

        // JLabel cnm = new JLabel("New label");
        // cnm.setIcon(new
        // ImageIcon("C:\\Users\\63569\\Desktop\\11\\P3_files\\guide-thumbnail-p3.png"));
        // cnm.setBounds(0, 0, 1024, 1024);
        // getContentPane().add(cnm);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(8, 8, 310, 220);
        contentPane.add(scroll);

        JLabel lblNewLabel = new JLabel("书名");
        lblNewLabel.setFont(nf);
        lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
        lblNewLabel.setBounds(328, 10, 27, 23);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("作者");
        lblNewLabel_1.setFont(nf);
        lblNewLabel_1.setBounds(328, 50, 54, 15);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("存库");
        lblNewLabel_2.setFont(nf);
        lblNewLabel_2.setBounds(328, 90, 54, 15);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("类型");
        lblNewLabel_3.setFont(nf);
        lblNewLabel_3.setBounds(328, 130, 54, 15);
        contentPane.add(lblNewLabel_3);

        JLabel lbName = new JLabel("");
        lbName.setBounds(365, 10, 200, 15);
        contentPane.add(lbName);

        JLabel lbAuthor = new JLabel("");
        lbAuthor.setBounds(365, 50, 200, 15);
        contentPane.add(lbAuthor);

        JLabel lbCount = new JLabel("");
        lbCount.setBounds(365, 90, 200, 15);
        contentPane.add(lbCount);

        JLabel lbType = new JLabel("");
        lbType.setBounds(365, 130, 200, 15);
        contentPane.add(lbType);

        JButton btnNewButton = new JButton("查询！");
        btnNewButton.setFont(nf);
        btnNewButton.setBounds(250, 300, 100, 50);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("刷新");
        btnNewButton_1.setFont(nf);
        btnNewButton_1.setBounds(100, 300, 100, 50);
        contentPane.add(btnNewButton_1);

        JButton FindBook = new JButton("查找书籍");
        FindBook.setFont(nf);
        FindBook.setBounds(400, 300, 100, 50);
        contentPane.add(FindBook);

        FindBook.addActionListener(arg -> {
            Find();
        });

        btnNewButton_1.addActionListener(arg -> {
            try {
                BookList = DbManager.getInstance().getAllBooks();
            } catch (SQLException e) {
                System.out.println(e);
            }
        });

        btnNewButton.addActionListener(arg -> {
            lbName.setText(list.getSelectedValue().bk.getName());
            lbAuthor.setText(list.getSelectedValue().bk.getAuthor());
            lbCount.setText(String.valueOf(list.getSelectedValue().bk.getCount()));
            lbType.setText(String.valueOf(list.getSelectedValue().bk.getType()));
        });

        contentPane.setLayout(null);

        frame.setVisible(true);
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

    public static void FindSuccess(JFrame frame) {
        frame.dispose();
    }

    public static void Find() {
        JFrame frame = new JFrame("查询界面");
        frame.setSize(270, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        frame.add(panel);

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
        lkfButton.setBounds(85, 80, 80, 25);
        panel.add(lkfButton);
        lkfButton.addActionListener(arg -> {
            try {
                if (BookName.getText().equals("")) {
                    if (author.getText().equals("")) {

                    } else {
                        books = DbManager.getInstance().getBooksWithAuthor(author.getText());
                    }
                } else {
                    if (author.getText().equals("")) {
                        books = DbManager.getInstance().getBooksWithName(BookName.getText());
                    } else {
                        books = DbManager.getInstance().getBooksWithNameAndAuthor(BookName.getText(), author.getText());
                    }
                }
                // BookList = books;
            } catch (Exception e) {

            }
            if (books.size() == 0) {
                FindFail(frame);
            } else {
                FindSuccess(frame);
            }
            // FindFail(frame);
            frame.setEnabled(false);
        });

        frame.setVisible(true);
    }

    private static void Login() {
        JFrame frame = new JFrame("登录界面");
        frame.setSize(270, 230);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        panel.setBackground(Color.WHITE);

        JLabel userLabel = new JLabel("账号:");
        userLabel.setFont(new Font("宋体", Font.PLAIN, 23));
        userLabel.setBounds(10, 20, 80, 30);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(70, 20, 165, 30);
        panel.add(userText);

        JLabel password = new JLabel("密码:");
        password.setFont(new Font("宋体", Font.PLAIN, 23));
        password.setBounds(10, 60, 80, 30);
        panel.add(password);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(70, 60, 165, 30);
        panel.add(passwordText);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(85, 100, 80, 30);
        panel.add(loginButton);

        JButton RegisterButton = new JButton("Register");
        RegisterButton.setBounds(85, 140, 80, 30);
        panel.add(RegisterButton);

        loginButton.addActionListener(args -> {
            String s = String.valueOf(passwordText.getPassword());
            System.out.println("账号：" + userText.getText() + "   密码：" + s);
            ListBooks();
            frame.dispose();
        });
        RegisterButton.addActionListener(args -> {
            String s = String.valueOf(passwordText.getPassword());
            System.out.println("账号：" + userText.getText() + "   密码：" + s);
            // frame.dispose();
        });

        // JLabel cnm = new JLabel("New label");
        // cnm.setIcon(new
        // ImageIcon("C:\\Users\\63569\\Desktop\\11\\P3_files\\guide-thumbnail-p3.png"));
        // cnm.setBounds(0, 0, 1024, 1024);
        // frame.add(cnm);

        // panel.setLayout(null);

        frame.setVisible(true);
    }
    // placeComponents

}