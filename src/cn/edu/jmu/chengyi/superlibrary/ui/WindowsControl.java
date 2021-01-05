package cn.edu.jmu.chengyi.superlibrary.ui;

import cn.edu.jmu.chengyi.superlibrary.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private static Font nf = new Font("宋体", Font.PLAIN, 13);
    public static User user;
    public static List<Book> BookList;
    static List<Book> books = new ArrayList<Book>();
    public static UserPermission us = UserPermission.ADMIN;
    public static String usname = "DASABI";

    private static void ListBooks() {
        JFrame frame = new JFrame();
        BookList = new ArrayList<>();
        frame.setBounds(100, 100, 600, 502);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));

        frame.add(contentPane);

        JList<ShowBook> list = new JList<ShowBook>();
        list.setFont(nf);
        list.setModel(new LLst());
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(8, 8, 310, 220);
        contentPane.add(scroll);

        try {
            BookList = DbManager.getInstance().getAllBooks();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        list.setListData(BookList.stream().map(ShowBook::new).toArray(ShowBook[]::new));

        JLabel lblNewLabel = new JLabel("书名");
        lblNewLabel.setFont(nf);
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

        JButton btnNewButton = new JButton("删除");
        btnNewButton.setFont(nf);
        btnNewButton.setBounds(250, 300, 100, 50);
        contentPane.add(btnNewButton);
        btnNewButton.addActionListener((args) -> {
            int index = list.getSelectedIndex();
            if (index < 0) return;
            Book b = list.getModel().getElementAt(index).bk;
            try {
                DbManager.getInstance().removeBook(b.getId());
                BookList = DbManager.getInstance().getAllBooks();
                list.setListData(BookList.stream().map(ShowBook::new).toArray(ShowBook[]::new));
                list.setSelectedIndex(0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        JButton btnNewButton_1 = new JButton("刷新");
        btnNewButton_1.setFont(nf);
        btnNewButton_1.setBounds(100, 300, 100, 50);
        contentPane.add(btnNewButton_1);

        JButton FindBook = new JButton("查找书籍");
        FindBook.setFont(nf);
        FindBook.setBounds(400, 300, 100, 50);
        contentPane.add(FindBook);

        JButton SetBook = new JButton("修改选中书籍");
        SetBook.setFont(nf);
        SetBook.setBounds(120, 400, 120, 50);
        contentPane.add(SetBook);

        JButton addBook = new JButton("添加书籍");
        addBook.setFont(nf);
        addBook.setBounds(360, 400, 120, 50);
        contentPane.add(addBook);
        addBook.addActionListener((args) -> AddNewBook());

        SetBook.addActionListener(args -> {
            SetBooks(list.getSelectedValue().bk);
            try {
                BookList = DbManager.getInstance().getAllBooks();
                list.setListData(BookList.stream().map(ShowBook::new).toArray(ShowBook[]::new));
                list.setSelectedIndex(0);
            } catch (SQLException throwables) {
                System.out.println("系统自动加载失败");
            }
        });
        FindBook.addActionListener(arg -> {
            Find(list);
        });

        list.addListSelectionListener(args -> {
            int index = list.getSelectedIndex();
            if (index < 0) return;
            Book b = list.getModel().getElementAt(index).bk;
            lbName.setText(b.getName());
            lbAuthor.setText(b.getAuthor());
            lbCount.setText(String.valueOf(b.getCount()));
            lbType.setText(String.valueOf(b.getType()));
        });
        btnNewButton_1.addActionListener(arg -> {
            try {
                BookList = DbManager.getInstance().getAllBooks();
                list.setListData(BookList.stream().map(ShowBook::new).toArray(ShowBook[]::new));
                list.setSelectedIndex(0);
            } catch (SQLException e) {
                System.out.println(e);
            }
        });

        contentPane.setLayout(null);

        frame.setVisible(true);
    }

    public static void MassageBox(JFrame up, String output) {
        JOptionPane.showConfirmDialog(up, output, output, -1, 2);
    }

    public static void FindSuccess(JFrame frame) {
        frame.dispose();
    }

    public static void Find(JList<ShowBook> list) {
        JFrame frame = new JFrame("查询界面");
        frame.setSize(270, 200);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            books.clear();
            String bookName = BookNameText.getText();
            String auth = authorText.getText();
            try {
                if (bookName.isEmpty()) {
                    if (!auth.isEmpty()) {
                        books = DbManager.getInstance().getBooksWithAuthor(auth);
                    }
                } else {
                    if (auth.isEmpty()) {
                        books = DbManager.getInstance().getBooksWithName(bookName);
                    } else {
                        books = DbManager.getInstance().getBooksWithNameAndAuthor(auth, bookName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (books.size() == 0) {
                MassageBox(frame, "查无此书");
            } else {
                list.setListData(books.stream().map(ShowBook::new).toArray(ShowBook[]::new));
                FindSuccess(frame);
            }
        });

        frame.setVisible(true);
    }

    public static void Login() {
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

        JButton jump = new JButton("hack");
        jump.setBounds(85, 140, 80, 30);
        panel.add(jump);
        jump.addActionListener((args) -> {
            user = User.EMPTY;
            EventQueue.invokeLater(() -> {
                Start();
                frame.dispose();
            });
        });

        loginButton.addActionListener(args -> {
            String s = String.valueOf(passwordText.getPassword());
            System.out.println("账号：" + userText.getText() + "   密码：" + s);
            try {
                Optional<User> us = DbManager.getInstance().getUser(userText.getText());//
                if (us.isPresent()) {
                    if (us.get().getPwd().equals(DbManager.SHA1(String.valueOf(passwordText.getPassword())))) {
                        Start();
                        user = us.get();
                        frame.dispose();
                    } else {
                        MassageBox(frame, "账号或密码错误");
                    }
                } else {
                    MassageBox(frame, "账号或密码错误");
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        });
        frame.setVisible(true);
    }

    private static void AddNewBook() {
        JTextField textField;
        JTextField textField_1;
        JTextField textField_2;
        JTextField textField_3;
        JTextField textField_4;
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\63569\\Desktop\\未命名 -1.png"));
        frame.setTitle("添加书籍");
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 342, 408);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);

        JLabel lblNewLabel = new JLabel("书名");
        lblNewLabel.setFont(new Font("新宋体", Font.PLAIN, 16));

        JLabel lblNewLabel_1 = new JLabel("类型");
        lblNewLabel_1.setFont(new Font("新宋体", Font.PLAIN, 16));

        textField = new JTextField();
        textField.setFont(new Font("新宋体", Font.PLAIN, 16));
        textField.setColumns(10);

        JComboBox<BookType> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("新宋体", Font.PLAIN, 16));
//        comboBox.setModel(new DefaultComboBoxModel(new String[]{"Science", "Education", "History", "Literature"}));
        for (BookType type : BookType.class.getEnumConstants()) {
            comboBox.addItem(type);
        }

        JLabel lblNewLabel_1_1 = new JLabel("数目");
        lblNewLabel_1_1.setFont(new Font("新宋体", Font.PLAIN, 16));

        textField_1 = new JTextField();
        textField_1.setFont(new Font("新宋体", Font.PLAIN, 16));
        textField_1.setColumns(10);

        JLabel lblNewLabel_1_1_1 = new JLabel("作者");
        lblNewLabel_1_1_1.setFont(new Font("新宋体", Font.PLAIN, 16));

        textField_2 = new JTextField();
        textField_2.setFont(new Font("新宋体", Font.PLAIN, 16));
        textField_2.setColumns(10);

        JLabel lblNewLabel_1_1_2 = new JLabel("价格");
        lblNewLabel_1_1_2.setFont(new Font("新宋体", Font.PLAIN, 16));

        textField_3 = new JTextField();
        textField_3.setFont(new Font("新宋体", Font.PLAIN, 16));
        textField_3.setColumns(10);

        JLabel lblNewLabel_1_1_2_1 = new JLabel("页码");
        lblNewLabel_1_1_2_1.setFont(new Font("新宋体", Font.PLAIN, 16));

        textField_4 = new JTextField();
        textField_4.setFont(new Font("新宋体", Font.PLAIN, 16));
        textField_4.setColumns(10);

        JButton btnNewButton = new JButton("决定添加！");
        btnNewButton.setFont(new Font("宋体", Font.PLAIN, 27));
        GroupLayout gl_contentPane = new GroupLayout(contentPane);

        /*
         *
         *
         *
         * 中间的内容是是自己编辑的
         *
         *
         *
         *
         */
        btnNewButton.addActionListener(arg -> {
            if (textField.getText().length() == 0) {
                MassageBox(frame, "书名不得为空");
                return;
            }
            if (textField_2.getText().length() == 0) {
                MassageBox(frame, "作者不得为空");
                return;
            }

            if (textField_1.getText().length() == 0) {
                MassageBox(frame, "数目不得为空");
                return;
            }

            if (textField_3.getText().length() == 0) {
                MassageBox(frame, "价格不得为空");
                return;
            }

            if (textField_4.getText().length() == 0) {
                MassageBox(frame, "页码不得为空");
                return;
            }
            try {
                Integer.valueOf(textField_1.getText());
                DbManager.getInstance().addBook(Enum.valueOf(BookType.class, ((BookType) comboBox.getSelectedItem()).name()),
                        textField.getText(), Integer.parseInt(textField_1.getText()), textField_2.getText(),
                        new BigDecimal(textField_3.getText()), Integer.parseInt(textField_4.getText()));
            } catch (NumberFormatException e) {
                MassageBox(frame, "输入的数量或者页码或者价格并不是合格数字");
                e.printStackTrace();
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
            MassageBox(frame, "添加成功！");
            frame.dispose();
        });
        /*
         *
         *
         *
         * 中间的内容是是自己编辑的
         *
         *
         *
         *
         */
        gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
                .createSequentialGroup().addGap(31)
                .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(lblNewLabel_1_1_2_1, GroupLayout.PREFERRED_SIZE, 32,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textField_4, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(lblNewLabel_1_1_2, GroupLayout.PREFERRED_SIZE, 32,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textField_3, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 32,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(lblNewLabel_1_1_1, GroupLayout.PREFERRED_SIZE, 32,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
                        .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
                                .addGroup(gl_contentPane.createSequentialGroup().addComponent(lblNewLabel)
                                        .addPreferredGap(ComponentPlacement.RELATED).addComponent(textField,
                                                GroupLayout.PREFERRED_SIZE, 209, GroupLayout.PREFERRED_SIZE))
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 32,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(comboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(40, Short.MAX_VALUE))
                .addGroup(gl_contentPane.createSequentialGroup().addGap(19)
                        .addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(22)));
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane
                .createSequentialGroup().addGap(23)
                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addComponent(
                        textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblNewLabel_1_1_1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblNewLabel_1_1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblNewLabel_1_1_2, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textField_3, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblNewLabel_1_1_2_1, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textField_4, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE)));
        contentPane.setLayout(gl_contentPane);
        frame.setVisible(true);
    }

    private static void Start() {

        JPanel contentPane;
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 297, 323);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);

        JLabel label = new JLabel("欢迎你");
        label.setFont(new Font("宋体", Font.PLAIN, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(10, 10, 261, 62);
        contentPane.add(label);

        JButton btnNewButton = new JButton("书籍");
        btnNewButton.setFont(new Font("宋体", Font.PLAIN, 16));
        btnNewButton.setBounds(92, 72, 101, 37);
        contentPane.add(btnNewButton);
        btnNewButton.addActionListener(args -> {
            ListBooks();
        });

        JButton btnNewButton_1 = new JButton("查阅管理");
        btnNewButton_1.setFont(new Font("宋体", Font.PLAIN, 16));
        btnNewButton_1.setBounds(92, 119, 101, 37);
        contentPane.add(btnNewButton_1);
        btnNewButton_1.addActionListener(args -> {
            UIManager.getInstance().showMainPanel();
        });
        JButton Borrowing = new JButton("退出登录");
        Borrowing.setFont(new Font("宋体", Font.PLAIN, 16));
        Borrowing.setBounds(92, 167, 101, 37);
        contentPane.add(Borrowing);

        JButton LogOut = new JButton("关闭系统");
        LogOut.setFont(new Font("宋体", Font.PLAIN, 16));
        LogOut.setBounds(92, 214, 101, 37);
        contentPane.add(LogOut);

        Borrowing.addActionListener(args -> {
            Login();
            frame.dispose();
        });

        LogOut.addActionListener(args -> {
            System.exit(0);
            frame.dispose();
        });

        contentPane.setLayout(null);
        frame.setVisible(true);
    }

    public static void SetBooks(Book bk) {
        JFrame frame = new JFrame();
        JPanel contentPane;

        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 312, 371);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("书名");
        lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel.setBounds(37, 29, 48, 28);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("作者");
        lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel_1.setBounds(37, 66, 48, 28);
        frame.getContentPane().add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("类型");
        lblNewLabel_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel_1_1.setBounds(37, 104, 48, 28);
        frame.getContentPane().add(lblNewLabel_1_1);

        JLabel lblNewLabel_1_1_1 = new JLabel("数目");
        lblNewLabel_1_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel_1_1_1.setBounds(37, 142, 48, 28);
        frame.getContentPane().add(lblNewLabel_1_1_1);

        JLabel lblNewLabel_1_1_1_1 = new JLabel("价格");
        lblNewLabel_1_1_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel_1_1_1_1.setBounds(37, 180, 48, 28);
        frame.getContentPane().add(lblNewLabel_1_1_1_1);

        JLabel lblNewLabel_1_1_1_1_1 = new JLabel("页码");
        lblNewLabel_1_1_1_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
        lblNewLabel_1_1_1_1_1.setBounds(37, 218, 48, 28);
        frame.getContentPane().add(lblNewLabel_1_1_1_1_1);

        JTextPane BookName = new JTextPane();
        BookName.setBounds(73, 29, 200, 28);
        frame.getContentPane().add(BookName);
        BookName.setText(bk.getName());

        JTextPane bookAuthor = new JTextPane();
        bookAuthor.setBounds(73, 67, 200, 28);
        frame.getContentPane().add(bookAuthor);
        bookAuthor.setText(bk.getAuthor());

        JComboBox<BookType> bookType = new JComboBox<>();
        bookType.setBounds(73, 108, 169, 23);
        for (BookType t : BookType.class.getEnumConstants()) {
            bookType.addItem(t);
        }
//        bookType.setModel(new DefaultComboBoxModel(new String[]{"Science", "Education", "History", "Literature"}));
        int ind = 0;
        if (BookType.Science.name().equals(bk.getType().name()))
            ind = 0;
        if (BookType.Education.name().equals(bk.getType().name()))
            ind = 1;
        if (BookType.History.name().equals(bk.getType().name()))
            ind = 2;
        if (BookType.Literature.name().equals(bk.getType().name()))
            ind = 3;
        bookType.setSelectedIndex(ind);
        frame.getContentPane().add(bookType);

        JTextPane bookCount = new JTextPane();
        bookCount.setBounds(73, 142, 200, 28);
        frame.getContentPane().add(bookCount);
        bookCount.setText(String.valueOf(bk.getCount()));

        JTextPane bookPrice = new JTextPane();
        bookPrice.setBounds(73, 180, 200, 28);
        frame.getContentPane().add(bookPrice);
        bookPrice.setText(String.valueOf(bk.getPrice()));

        JTextPane bookPage = new JTextPane();
        bookPage.setBounds(73, 218, 200, 28);
        frame.getContentPane().add(bookPage);
        bookPage.setText(String.valueOf(bk.getPage()));

        JButton btnNewButton = new JButton("确认修改");
        btnNewButton.setBounds(98, 273, 93, 23);
        frame.getContentPane().add(btnNewButton);

        btnNewButton.addActionListener(args -> {
            if (BookName.getText().length() == 0) {
                MassageBox(frame, "书名不得为空");
                return;
            }
            if (bookAuthor.getText().length() == 0) {
                MassageBox(frame, "作者不得为空");
                return;
            }
            if (bookCount.getText().length() == 0) {
                MassageBox(frame, "数目不得为空");
                return;
            }
            if (bookPrice.getText().length() == 0) {
                MassageBox(frame, "价格不得为空");
                return;
            }
            if (bookPage.getText().length() == 0) {
                MassageBox(frame, "页码不得为空");
                return;
            }


//            if (BookName.getText().equals(String.valueOf(bk.getName()))) {
//                try {
//                    DbManager.getInstance().setBook(bk, DbManager.TAG_NAME, BookName.getText());
//                } catch (NumberFormatException | SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (bookAuthor.getText().equals(String.valueOf(bk.getAuthor()))) {
//                try {
//                    DbManager.getInstance().setBook(bk, DbManager.TAG_AUTHOR, bookAuthor.getText());
//                } catch (NumberFormatException | SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (bookCount.getText().equals(String.valueOf(bk.getCount()))) {
//                try {
//                    DbManager.getInstance().setBook(bk, DbManager.TAG_COUNT,
//                            (int) Integer.valueOf(bookCount.getText()));
//                } catch (NumberFormatException | SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (bookPrice.getText().equals(String.valueOf(bk.getPrice()))) {
//                try {
//                    DbManager.getInstance().setBook(bk, DbManager.TAG_PRICE,
//                            (int) Integer.valueOf(bookPrice.getText()));
//                } catch (NumberFormatException | SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (bookPage.getText().equals(String.valueOf(bk.getPage()))) {
//                try {
//                    DbManager.getInstance().setBook(bk, DbManager.TAG_PAGE, (int) Integer.valueOf(bookPage.getText()));
//                } catch (NumberFormatException | SQLException e) {
//                    e.printStackTrace();
//                }
//            }
            BookProperty property = new BookProperty();

            property.setName(BookName.getText());
            property.setType((BookType) bookType.getSelectedItem());
            property.setAuthor(bookAuthor.getText());
            property.setCount(Integer.parseInt(bookCount.getText()));
            property.setPrice(new BigDecimal(bookPrice.getText()));
            property.setPage(Integer.parseInt(bookPage.getText()));

            try {
                DbManager.getInstance().setBook(bk, property);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            MassageBox(frame, "修改成功");
            frame.dispose();
            // if (BookName.getText().length() == 0) {
            // MassageBox(frame, "书名不得为空");
            // return;
            // }

        });

        frame.setVisible(true);
    }
}
