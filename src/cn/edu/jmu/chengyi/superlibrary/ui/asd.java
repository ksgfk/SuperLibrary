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
import javax.swing.AbstractListModel;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class asd extends JFrame {

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
        
        
        // setContentPane(contentPane);
//        
//        
//        JTextArea textArea = new JTextArea();
//        textArea.setBounds(84, 142, 108, 192);
//        
//        
//        //((Object) scrollBar).setViewPortView(textArea);
//        
//        getContentPane().add(textArea);
//        
//        JScrollBar scrollBar = new JScrollBar();
//        scrollBar.setBounds(190, 142, 17, 192);
//        getContentPane().add(scrollBar);
        getContentPane().setLayout(null);
        
        JList list = new JList();
        list.setBorder(new LineBorder(new Color(0, 0, 0)));
        list.setModel(new AbstractListModel() {
        	String[] values = new String[] {"1", "1", "12341", "1", "64324", "1", "1", "43432432", "423", "4", "235", "34", "623454"};
        	public int getSize() {
        		return values.length;
        	}
        	public Object getElementAt(int index) {
        		return values[index];
        	}
        });
        list.setSelectedIndex(20);
        list.setBounds(322, 173, 49, 132);
        getContentPane().add(list);
        
        JScrollBar scrollBar = new JScrollBar();
        scrollBar.setBounds(372, 173, 17, 132);
        getContentPane().add(scrollBar);

    }
}
