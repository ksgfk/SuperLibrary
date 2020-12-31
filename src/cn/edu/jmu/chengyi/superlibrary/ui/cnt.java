package cn.edu.jmu.chengyi.superlibrary.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.Toolkit;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

public class cnt extends JFrame {

    private JPanel contentPane;
    private JTextPane bookAuthor;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    cnt frame = new cnt();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public cnt() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\63569\\Desktop\\未命名 -1.png"));
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 297, 323);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
//        setContentPane(contentPane);
        getContentPane().setLayout(null);
        
        JLabel label = new JLabel("欢迎你");
        label.setFont(new Font("宋体", Font.PLAIN, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(10, 10, 261, 62);
        getContentPane().add(label);
        
        JButton btnNewButton = new JButton("书籍");
        btnNewButton.setFont(new Font("宋体", Font.PLAIN, 16));
        btnNewButton.setBounds(92, 72, 101, 37);
        getContentPane().add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("查阅管理");
        btnNewButton_1.setFont(new Font("宋体", Font.PLAIN, 16));
        btnNewButton_1.setBounds(92, 119, 101, 37);
        getContentPane().add(btnNewButton_1);
        
        JButton btnNewButton_1_1 = new JButton("退出登录");
        btnNewButton_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
        btnNewButton_1_1.setBounds(92, 167, 101, 37);
        getContentPane().add(btnNewButton_1_1);
        
        JButton btnNewButton_1_1_1 = new JButton("关闭系统");
        btnNewButton_1_1_1.setFont(new Font("宋体", Font.PLAIN, 16));
        btnNewButton_1_1_1.setBounds(92, 214, 101, 37);
        getContentPane().add(btnNewButton_1_1_1);
        
        

    }
}
