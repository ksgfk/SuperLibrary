package cn.edu.jmu.chengyi.superlibrary.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.ScrollPane;

import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.management.ValueExp;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.border.SoftBevelBorder;

import com.mysql.fabric.xmlrpc.base.Value;

import cn.edu.jmu.chengyi.superlibrary.Book;
import cn.edu.jmu.chengyi.superlibrary.User;

import javax.swing.border.BevelBorder;

/**
 * wdnmd
 */
class wdnmd {
    public int a;
    public int b;

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setB(int b) {
        this.b = b;
    }

    wdnmd(int a, int b) {
        setA(a);
        setB(b);
    }

    @Override
    public String toString() {
        return a + " ";
    }

    public String newString() {
        return a + b + " ";
    }
}

public class asd extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    asd frame = new asd();
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
    public asd() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 502);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        getContentPane().setLayout(null);

        JList<wdnmd> list = new JList<wdnmd>();
        list.setModel(new AbstractListModel<wdnmd>() {
            private static final long serialVersionUID = 1L;
            wdnmd values[] = new wdnmd[1];
            {
                values[0] = new wdnmd(1, 2);
            }

            public int getSize() {
                return values.length;
            }

            public wdnmd getElementAt(int index) {
                return values[index];
            }
        });

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(8, 8, 310, 220);
        getContentPane().add(scroll);

        JButton btnNewButton = new JButton("New button");
        btnNewButton.setBounds(232, 338, 93, 23);
        getContentPane().add(btnNewButton);

        JLabel lblNewLabel = new JLabel("书名");
        lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
        lblNewLabel.setBounds(328, 10, 27, 23);
        getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("作者");
        lblNewLabel_1.setBounds(328, 43, 54, 15);
        getContentPane().add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("存库");
        lblNewLabel_2.setBounds(328, 75, 54, 15);
        getContentPane().add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("类型");
        lblNewLabel_3.setBounds(328, 117, 54, 15);
        getContentPane().add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("");
        lblNewLabel_4.setBounds(365, 10, 54, 15);
        getContentPane().add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("");
        lblNewLabel_5.setBounds(365, 43, 54, 15);
        getContentPane().add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("");
        lblNewLabel_6.setBounds(365, 75, 54, 15);
        getContentPane().add(lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel("");
        lblNewLabel_7.setBounds(365, 117, 54, 15);
        getContentPane().add(lblNewLabel_7);

        btnNewButton.addActionListener(arg -> {
            // lblNewLabel.setText(list.getSelectedValue().newString() + "\ncnm nmsl ");
            // list.getSelectedValue().newString();
        });
    }
}
