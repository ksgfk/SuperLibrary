package cn.edu.jmu.chengyi.superlibrary.ui;

import cn.edu.jmu.chengyi.superlibrary.Book;
import cn.edu.jmu.chengyi.superlibrary.BorrowLog;
import cn.edu.jmu.chengyi.superlibrary.DbManager;
import cn.edu.jmu.chengyi.superlibrary.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.*;

public class UserBorrowPanel extends JFrame {
    private static final Vector<String> COLUMN_OBJ = new Vector<String>() {
        {
            add("记录ID");
            add("书名");
            add("借书时间");
            add("还书时间");
            add("是否返还");
        }
    };

    private static class TableModel extends DefaultTableModel {
        public TableModel(Vector<? extends Vector> data, Vector<?> columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private final Vector<Vector<Object>> data;
    private final JTable table;
    private final JLabel userName;
    private User holdUser;

    public UserBorrowPanel() {
        data = new Vector<>();
        addWindowListener(new UIManager.UIWindowEvent(this));
        setBounds(100, 100, 600, 500);
        getContentPane().setLayout(null);

        JPanel rootPanel = new JPanel(null);
        rootPanel.setLayout(null);
        setContentPane(rootPanel);

        table = new JTable(new TableModel(data, COLUMN_OBJ));
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(true);
        table.setFont(UIManager.getNormalFont());
        table.getTableHeader().setFont(UIManager.getNormalFont());
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tablePanel = new JScrollPane(table);
        tablePanel.setSize(565, 405);
        tablePanel.setLocation(10, 50);
        tablePanel.setFont(UIManager.getNormalFont());
        tablePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        rootPanel.add(tablePanel);

        JPanel btnPanel = new JPanel();
        btnPanel.setBounds(10, 10, 565, 33);
        rootPanel.add(btnPanel);
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel user = new JLabel("用户:");
        btnPanel.add(user);

        userName = new JLabel();
        btnPanel.add(userName);

        JButton returnBtn = new JButton("还书");
        btnPanel.add(returnBtn);
        returnBtn.addActionListener((args) -> {
            int row = table.getSelectedRow();
            if (row < 0) return;
            Boolean id = (Boolean) table.getModel().getValueAt(row, 4);
            if (id) {
                UIManager.getInstance().errorMessage("已经还过书了");
                return;
            }
            try {
                DbManager.getInstance().setBookReturn((Integer) table.getModel().getValueAt(row, 0), true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            refreshData();
        });

        JButton addBtn = new JButton("添加");
        btnPanel.add(addBtn);
        addBtn.addActionListener((args) -> {
            try {
                DbManager.getInstance().addBorrowLog(holdUser.getId(), -1, new Date(0), new Date(0), false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            refreshData();
        });

        JButton changeBtn = new JButton("修改记录");
        btnPanel.add(changeBtn);
        changeBtn.addActionListener((args) -> {
            int row = table.getSelectedRow();
            if (row < 0) return;
            Integer id = (Integer) table.getModel().getValueAt(row, 0);
            BorrowLog log;
            try {
                Optional<BorrowLog> l = DbManager.getInstance().getBorrowLog(id);
                log = l.orElseThrow(NoSuchElementException::new);
            } catch (SQLException | NoSuchElementException e) {
                e.printStackTrace();
                return;
            }
            EventQueue.invokeLater(() -> UIManager.getInstance().showBorrowInfo(log));
        });

        JButton refreshBtn = new JButton("刷新");
        btnPanel.add(refreshBtn);
        refreshBtn.addActionListener((args) -> refreshData());

        setLocationRelativeTo(null);
    }

    public void onShow(User user) {
        holdUser = user;
        userName.setText(holdUser.getName());
        refreshData();
        setVisible(true);
    }

    public void refreshData() {
        List<BorrowLog> logs;
        try {
            logs = DbManager.getInstance().getBorrowLog(holdUser);
        } catch (Exception e) {
            UIManager.getInstance().errorMessage(e.getMessage());
            return;
        }
        data.clear();
        for (BorrowLog u : logs) {
            Vector<Object> v = new Vector<>(4);
            data.add(v);
            v.add(u.getId());
            try {
                Optional<Book> b = DbManager.getInstance().getBook(u.getBookId());
                v.add(b.orElseThrow(NoSuchElementException::new).getName());
            } catch (SQLException | NoSuchElementException e) {
                v.add("查无此书");
            }
            v.add(BorrowLog.TIME_FORMATTER.format(u.getStartTime()));
            v.add(BorrowLog.TIME_FORMATTER.format(u.getEndTime()));
            v.add(u.isReturn());
        }
        ((DefaultTableModel) table.getModel()).fireTableStructureChanged();
        rootPane.updateUI();
    }
}
