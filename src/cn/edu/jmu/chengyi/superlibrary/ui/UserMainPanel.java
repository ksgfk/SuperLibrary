package cn.edu.jmu.chengyi.superlibrary.ui;

import cn.edu.jmu.chengyi.superlibrary.DbManager;
import cn.edu.jmu.chengyi.superlibrary.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class UserMainPanel extends JFrame {
    private static final Vector<String> COLUMN_OBJ = new Vector<String>() {
        {
            add("ID");
            add("用户名");
            add("密码");
            add("权限组");
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

    public UserMainPanel() {
        data = new Vector<>();
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

        JButton addUsrBtn = new JButton("添加");
        addUsrBtn.addActionListener(args -> {
            try {
                DbManager.getInstance().addUser("", "", DbManager::SHA1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventQueue.invokeLater(this::refreshData);
            EventQueue.invokeLater(() -> {
                User hasAdded;
                try {
                    hasAdded = DbManager.getInstance().getUser((Integer) data.get(data.size() - 1).get(0)).orElseThrow();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                UIManager.getInstance().showUserInfo(hasAdded);
            });
        });
        btnPanel.add(addUsrBtn);

        JButton updateUsrBtn = new JButton("更新信息");
        btnPanel.add(updateUsrBtn);
        updateUsrBtn.addActionListener(args -> {
            int row = table.getSelectedRow();
            if (row < 0) return;
            int id = (Integer) table.getModel().getValueAt(row, 0);
            Optional<User> u;
            try {
                u = DbManager.getInstance().getUser(id);
            } catch (Exception e) {
                UIManager.getInstance().errorMessage(e.getMessage());
                return;
            }
            if (u.isEmpty()) {
                UIManager.getInstance().errorMessage("User不存在");
                return;
            }
            final User user = u.get();
            EventQueue.invokeLater(() -> UIManager.getInstance().showUserInfo(user));
        });

        JButton rmUsrBtn = new JButton("删除");
        rmUsrBtn.addActionListener(args -> {
            int row = table.getSelectedRow();
            if (row < 0) return;
            int id = (Integer) table.getModel().getValueAt(row, 0);
            try {
                DbManager.getInstance().removeUser(id);
            } catch (Exception e) {
                UIManager.getInstance().errorMessage(e);
            }
            EventQueue.invokeLater(this::refreshData);
        });
        btnPanel.add(rmUsrBtn);

        JButton refreshBtn = new JButton("刷新");
        btnPanel.add(refreshBtn);
        refreshBtn.addActionListener((args) -> refreshData());

        setLocationRelativeTo(null);
    }

    public void onShow() {
        refreshData();
        setVisible(true);
    }

    public void refreshData() {
        List<User> users;
        try {
            users = DbManager.getInstance().getAllUser();
        } catch (Exception e) {
            UIManager.getInstance().errorMessage(e.getMessage());
            return;
        }
        data.clear();//TODO:想想办法不每次都new
        for (User u : users) {
            Vector<Object> v = new Vector<>(4);
            data.add(v);
            v.add(u.getId());
            v.add(u.getName());
            v.add(u.getPwd());
            v.add(u.getPermission());
        }
        ((DefaultTableModel) table.getModel()).fireTableStructureChanged();
        rootPane.updateUI();
    }
}
