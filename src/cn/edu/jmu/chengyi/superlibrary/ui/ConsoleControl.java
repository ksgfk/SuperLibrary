
/*
 * 
 * package cn.edu.jmu.chengyi.superlibrary.ui;
 * 
 * import java.awt.BorderLayout; import java.awt.EventQueue; import
 * java.awt.Font;
 * 
 * import javax.swing.JFrame; import javax.swing.JPanel; import
 * javax.swing.border.EmptyBorder; import javax.swing.JLabel; import
 * java.sql.SQLException; import java.util.ArrayList; import java.util.List;
 * 
 * import javax.swing.SwingConstants; import javax.swing.JList; import
 * javax.swing.JScrollPane; import javax.swing.AbstractListModel; import
 * javax.swing.ImageIcon; import javax.swing.JButton;
 * 
 * import cn.edu.jmu.chengyi.superlibrary.Book; import
 * cn.edu.jmu.chengyi.superlibrary.DbManager;
 * 
 * class ShowBook { Book bk;
 * 
 * ShowBook(Book b) { bk = b; }
 * 
 * public String toString() { return bk.getName(); } }
 * 
 * class LLst extends AbstractListModel<ShowBook> { private static final long
 * serialVersionUID = 1L; ConsoleControl tp; int size;
 * 
 * LLst(ConsoleControl temp) { tp = temp; }
 * 
 * public int getSize() { return tp.BookList.size(); }
 * 
 * public ShowBook getElementAt(int index) { return new
 * ShowBook(tp.BookList.get(index)); } }
 * 
 * public class ConsoleControl extends JFrame {
 * 
 * List<Book> BookList; private static final long serialVersionUID = 1L; private
 * JPanel contentPane;
 * 
 * public static void main(String[] args) { EventQueue.invokeLater(new
 * Runnable() { public void run() { try { ConsoleControl frame = new
 * ConsoleControl(); frame.setVisible(true); } catch (Exception e) {
 * e.printStackTrace(); } } }); }
 * 
 * public ConsoleControl() { Font nf = new Font("宋体", Font.PLAIN, 13); BookList
 * = new ArrayList<>(); BookList.add(Book.NoonBook);
 * setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setBounds(100, 100, 600,
 * 502); contentPane = new JPanel(); contentPane.setBorder(new EmptyBorder(5, 5,
 * 5, 5)); contentPane.setLayout(new BorderLayout(0, 0));
 * 
 * JList<ShowBook> list = new JList<ShowBook>(); list.setFont(nf);
 * list.setModel(new LLst(this));
 * 
 * // JLabel cnm = new JLabel("New label"); // cnm.setIcon(new //
 * ImageIcon("C:\\Users\\63569\\Desktop\\11\\P3_files\\guide-thumbnail-p3.png"))
 * ; // cnm.setBounds(0, 0, 1024, 1024); // getContentPane().add(cnm);
 * 
 * JScrollPane scroll = new JScrollPane(list); scroll.setBounds(8, 8, 310, 220);
 * getContentPane().add(scroll);
 * 
 * JLabel lblNewLabel = new JLabel("书名"); lblNewLabel.setFont(nf);
 * lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
 * lblNewLabel.setBounds(328, 10, 27, 23); getContentPane().add(lblNewLabel);
 * 
 * JLabel lblNewLabel_1 = new JLabel("作者"); lblNewLabel_1.setFont(nf);
 * lblNewLabel_1.setBounds(328, 50, 54, 15);
 * getContentPane().add(lblNewLabel_1);
 * 
 * JLabel lblNewLabel_2 = new JLabel("存库"); lblNewLabel_2.setFont(nf);
 * lblNewLabel_2.setBounds(328, 90, 54, 15);
 * getContentPane().add(lblNewLabel_2);
 * 
 * JLabel lblNewLabel_3 = new JLabel("类型"); lblNewLabel_3.setFont(nf);
 * lblNewLabel_3.setBounds(328, 130, 54, 15);
 * getContentPane().add(lblNewLabel_3);
 * 
 * JLabel lbName = new JLabel(""); lbName.setBounds(365, 10, 200, 15);
 * getContentPane().add(lbName);
 * 
 * JLabel lbAuthor = new JLabel(""); lbAuthor.setBounds(365, 50, 200, 15);
 * getContentPane().add(lbAuthor);
 * 
 * JLabel lbCount = new JLabel(""); lbCount.setBounds(365, 90, 200, 15);
 * getContentPane().add(lbCount);
 * 
 * JLabel lbType = new JLabel(""); lbType.setBounds(365, 130, 200, 15);
 * getContentPane().add(lbType);
 * 
 * JButton btnNewButton = new JButton("查询！"); btnNewButton.setFont(nf);
 * btnNewButton.setBounds(250, 300, 100, 50);
 * getContentPane().add(btnNewButton);
 * 
 * JButton btnNewButton_1 = new JButton("刷新"); btnNewButton_1.setFont(nf);
 * btnNewButton_1.setBounds(100, 300, 100, 50);
 * getContentPane().add(btnNewButton_1);
 * 
 * JButton FindBook = new JButton("查找书籍"); FindBook.setFont(nf);
 * FindBook.setBounds(400, 300, 100, 50); getContentPane().add(FindBook);
 * 
 * FindBook.addActionListener(arg -> { WindowsControl.Find(); });
 * 
 * btnNewButton_1.addActionListener(arg -> { try { BookList =
 * DbManager.getInstance().getAllBooks(); } catch (SQLException e) {
 * System.out.println(e); } });
 * 
 * btnNewButton.addActionListener(arg -> {
 * lbName.setText(list.getSelectedValue().bk.getName());
 * lbAuthor.setText(list.getSelectedValue().bk.getAuthor());
 * lbCount.setText(String.valueOf(list.getSelectedValue().bk.getCount()));
 * lbType.setText(String.valueOf(list.getSelectedValue().bk.getType())); });
 * 
 * getContentPane().setLayout(null); } } [ 登录界面 √
 * 
 * 欢迎你sb 查询书本 √ 列举书本 √ 我的借阅 删除书本 增加书本 修改内容 借阅管理 退出登录
 * 
 */