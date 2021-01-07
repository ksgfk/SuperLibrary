package cn.edu.jmu.chengyi.superlibrary.ui;

import cn.edu.jmu.chengyi.superlibrary.DbManager;
import cn.edu.jmu.chengyi.superlibrary.User;
import cn.edu.jmu.chengyi.superlibrary.UserPermission;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.NoSuchElementException;
import java.util.Objects;

public class UserInfoPanel extends JFrame {
    private User holdUser;
    private final JLabel idValue;
    private final JTextField nameInputField;
    private final JTextField pwdInputField;
    private final JCheckBox isChangeBox;
    private final JComboBox<UserPermission> pmsCombo;

    private static class ValueChangeEvent implements DocumentListener {
        private final UserInfoPanel panel;

        private ValueChangeEvent(UserInfoPanel panel) {
            this.panel = panel;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            panel.onAnyValueChanged();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            panel.onAnyValueChanged();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            panel.onAnyValueChanged();
        }
    }

    private static class InfoPanelEvent extends UIManager.UIWindowEvent {
        public InfoPanelEvent(JFrame frame) {
            super(frame);
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            super.windowDeactivated(e);
            EventQueue.invokeLater(UIManager.getInstance().getUserMainPanel()::refreshData);
        }
    }

    public UserInfoPanel() {
        addWindowListener(new InfoPanelEvent(this));
        setBounds(100, 100, 450, 300);
        JPanel rootPanel = new JPanel();
        rootPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(rootPanel);
        rootPanel.setLayout(null);
        rootPanel.setFont(UIManager.getNormalFont());

        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(10, 20, 416, 175);
        rootPanel.add(infoPanel);
        infoPanel.setLayout(new GridLayout(5, 1, 0, 10));

        JPanel idPanel = new JPanel();
        infoPanel.add(idPanel);
        idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));

        JLabel idConst = new JLabel("ID:");
        idPanel.add(idConst);
        idConst.setFont(UIManager.getNormalFont());

        idValue = new JLabel("placeholder");
        idPanel.add(idValue);
        idValue.setFont(UIManager.getNormalFont());

        JPanel namePanel = new JPanel();
        infoPanel.add(namePanel);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        JLabel nameConst = new JLabel("名字:");
        namePanel.add(nameConst);
        nameConst.setFont(UIManager.getNormalFont());

        nameInputField = new JTextField();
        namePanel.add(nameInputField);
        nameInputField.setColumns(10);
        nameInputField.setFont(UIManager.getNormalFont());

        JPanel pwdPanel = new JPanel();
        infoPanel.add(pwdPanel);
        pwdPanel.setLayout(new BoxLayout(pwdPanel, BoxLayout.X_AXIS));

        JLabel pwdConst = new JLabel("密码:");
        pwdPanel.add(pwdConst);
        pwdConst.setFont(UIManager.getNormalFont());

        pwdInputField = new JTextField();
        pwdPanel.add(pwdInputField);
        pwdInputField.setColumns(10);
        pwdInputField.setFont(UIManager.getNormalFont());

        JButton cryBtn = new JButton("加密");
        pwdPanel.add(cryBtn);
        cryBtn.setFont(UIManager.getNormalFont());
        cryBtn.addActionListener(args -> pwdInputField.setText(DbManager.SHA1(pwdInputField.getText())));

        JPanel pmsPanel = new JPanel();
        infoPanel.add(pmsPanel);
        pmsPanel.setLayout(new BoxLayout(pmsPanel, BoxLayout.X_AXIS));

        JLabel pmsConst = new JLabel("权限组:");
        pmsPanel.add(pmsConst);
        pmsConst.setFont(UIManager.getNormalFont());

        pmsCombo = new JComboBox<>();
        pmsPanel.add(pmsCombo);
        for (UserPermission p : UserPermission.class.getEnumConstants()) {
            pmsCombo.addItem(p);
        }
        pmsCombo.setFont(UIManager.getNormalFont());

        JPanel isChangePanel = new JPanel();
        infoPanel.add(isChangePanel);
        isChangePanel.setLayout(new BoxLayout(isChangePanel, BoxLayout.X_AXIS));

        isChangeBox = new JCheckBox("是否更改");
        isChangeBox.setEnabled(false);
        isChangePanel.add(isChangeBox);

        JPanel btnPanel = new JPanel();
        btnPanel.setBounds(10, 216, 416, 37);
        rootPanel.add(btnPanel);

        JButton applyBtn = new JButton("应用");
        btnPanel.add(applyBtn);
        applyBtn.setFont(UIManager.getNormalFont());
        applyBtn.addActionListener(args -> {
            try {
                DbManager.getInstance().setUser(holdUser,
                        nameInputField.getText(),
                        pwdInputField.getText(),
                        (UserPermission) Objects.requireNonNull(pmsCombo.getSelectedItem()));
            } catch (Exception e) {
                UIManager.getInstance().errorMessage(e.getMessage());
                return;
            }
            isChangeBox.setSelected(false);
        });

        JButton refreshBtn = new JButton("刷新");
        btnPanel.add(refreshBtn);
        refreshBtn.setFont(UIManager.getNormalFont());
        refreshBtn.addActionListener(args -> {
            User u;
            try {
                u = DbManager.getInstance().getUser(holdUser.getId()).orElseThrow(NoSuchElementException::new);
            } catch (Exception e) {
                UIManager.getInstance().errorMessage(e.getMessage());
                return;
            }
            setInfo(u);
        });

        JButton resetBtn = new JButton("重置");
        btnPanel.add(resetBtn);
        resetBtn.setFont(UIManager.getNormalFont());
        resetBtn.addActionListener(args -> setInfo(holdUser));

        ValueChangeEvent inputEvent = new ValueChangeEvent(this);
        nameInputField.getDocument().addDocumentListener(inputEvent);
        pwdInputField.getDocument().addDocumentListener(inputEvent);
        pmsCombo.addItemListener((args) -> onAnyValueChanged());
    }

    public void onShow(User user) {
        holdUser = user;
        setInfo(user);
        isChangeBox.setSelected(false);
        setVisible(true);
    }

    private void onAnyValueChanged() {
        isChangeBox.setSelected(true);
    }

    private void setInfo(User user) {
        idValue.setText(String.valueOf(user.getId()));
        nameInputField.setText(user.getName());
        pwdInputField.setText(user.getPwd());
        pmsCombo.setSelectedItem(user.getPermission());
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING && isChangeBox.isSelected()) {
            int select = JOptionPane.showConfirmDialog(null, "还有未保存的项,是否退出?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (select == 1) return;
        }
        super.processWindowEvent(e);
    }
}
