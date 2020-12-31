package cn.edu.jmu.chengyi.superlibrary.ui;

import cn.edu.jmu.chengyi.superlibrary.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Stack;

public class UIManager {
    private static final Font normalFont = new Font("宋体", Font.PLAIN, 16);

    private static class Instance {
        private static final UIManager instance = new UIManager();
    }

    public static UIManager getInstance() {
        return Instance.instance;
    }

    public static Font getNormalFont() {
        return normalFont;
    }

    static class UIWindowEvent implements WindowListener {
        private final JFrame frame;

        public UIWindowEvent(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void windowOpened(WindowEvent e) {
        }

        @Override
        public void windowClosing(WindowEvent e) {
        }

        @Override
        public void windowClosed(WindowEvent e) {

        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {
            UIManager.getInstance().uiStack.push(frame);
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            UIManager.getInstance().uiStack.pop();
        }
    }

    private final Stack<JFrame> uiStack;
    private final UserMainPanel userMainPanel;
    private final UserInfoPanel userInfoPanel;

    private UIManager() {
        uiStack = new Stack<>();
        userMainPanel = new UserMainPanel();
        userMainPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userInfoPanel = new UserInfoPanel();
        userInfoPanel.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        EventQueue.invokeLater(() -> {
            userMainPanel.onShow();
            uiStack.push(userMainPanel);
        });
    }

    public UserMainPanel getUserMainPanel() {
        return userMainPanel;
    }

    public UserInfoPanel getUserInfoPanel() {
        return userInfoPanel;
    }

    public void showUserInfo(User user) {
        if (user == null) {
            errorMessage("User为空");
            return;
        }
        userInfoPanel.onShow(user);
    }

    public void errorMessage(Object msg) {
        JOptionPane.showMessageDialog(uiStack.peek(), msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
