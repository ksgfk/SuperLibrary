package cn.edu.jmu.chengyi.superlibrary.ui;

import cn.edu.jmu.chengyi.superlibrary.BorrowLog;
import cn.edu.jmu.chengyi.superlibrary.DbManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

public class UserBorrowInfoPanel extends JFrame {
    private static class InfoPanelEvent extends UIManager.UIWindowEvent {
        public InfoPanelEvent(JFrame frame) {
            super(frame);
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            super.windowDeactivated(e);
            EventQueue.invokeLater(UIManager.getInstance().getUserBorrowPanel()::refreshData);
        }
    }

    private final JLabel idValue;
    private final JTextField bookInputField;
    private final JLabel startTimeShow;
    private final JLabel endTimeShow;
    private final JCheckBox isReturnBox;
    private Date startTime;
    private Date endTime;
    private BorrowLog log;

    public UserBorrowInfoPanel() {
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

        JPanel bookPanel = new JPanel();
        infoPanel.add(bookPanel);
        bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.X_AXIS));

        JLabel bookIdConst = new JLabel("书本ID:");
        bookPanel.add(bookIdConst);
        bookIdConst.setFont(UIManager.getNormalFont());

        bookInputField = new JTextField();
        bookPanel.add(bookInputField);
        bookInputField.setColumns(10);
        bookInputField.setFont(UIManager.getNormalFont());

        JPanel startTimePanel = new JPanel();
        infoPanel.add(startTimePanel);
        startTimePanel.setLayout(new BoxLayout(startTimePanel, BoxLayout.X_AXIS));

        JLabel startTimeConst = new JLabel("借书日期:");
        startTimePanel.add(startTimeConst);
        startTimeConst.setFont(UIManager.getNormalFont());

        startTimeShow = new JLabel("placeholder");
        startTimePanel.add(startTimeShow);
        startTimeShow.setFont(UIManager.getNormalFont());

        JButton setDate = new JButton("设置日期");
        startTimePanel.add(setDate);
        setDate.setFont(UIManager.getNormalFont());
        setDate.addActionListener((args) -> {
            JTimeChooser chooser = new JTimeChooser(this);
            Calendar res = chooser.showTimeDialog();
            startTime = res.getTime();
            startTimeShow.setText(BorrowLog.TIME_FORMATTER.format(startTime));
        });

        JPanel endTimePanel = new JPanel();
        infoPanel.add(endTimePanel);
        endTimePanel.setLayout(new BoxLayout(endTimePanel, BoxLayout.X_AXIS));

        JLabel endTimeConst = new JLabel("还书日期:");
        endTimePanel.add(endTimeConst);
        endTimeConst.setFont(UIManager.getNormalFont());

        endTimeShow = new JLabel("placeholder");
        endTimePanel.add(endTimeShow);
        endTimeShow.setFont(UIManager.getNormalFont());

        JButton setDate2 = new JButton("设置日期");
        endTimePanel.add(setDate2);
        setDate2.setFont(UIManager.getNormalFont());
        setDate2.addActionListener((args) -> {
            JTimeChooser chooser = new JTimeChooser(this);
            Calendar res = chooser.showTimeDialog();
            endTime = res.getTime();
            endTimeShow.setText(BorrowLog.TIME_FORMATTER.format(endTime));
        });

        JPanel isReturnPanel = new JPanel();
        infoPanel.add(isReturnPanel);
        isReturnPanel.setLayout(new BoxLayout(isReturnPanel, BoxLayout.X_AXIS));

        JLabel isReturnConst = new JLabel("是否归还:");
        isReturnPanel.add(isReturnConst);
        isReturnConst.setFont(UIManager.getNormalFont());

        isReturnBox = new JCheckBox();
        isReturnPanel.add(isReturnBox);

        JPanel btnPanel = new JPanel();
        btnPanel.setBounds(10, 208, 416, 45);
        rootPanel.add(btnPanel);

        JButton applyButton = new JButton("应用");
        btnPanel.add(applyButton);
        applyButton.setFont(UIManager.getNormalFont());
        applyButton.addActionListener((args) -> {
            int inputBook = Integer.parseInt(bookInputField.getText());
            try {
                DbManager.getInstance().setBorrowLog(log, log.getBorrowerId(), inputBook, startTime, endTime, isReturnBox.isSelected());
                this.log = DbManager.getInstance().getBorrowLog(log.getId()).orElseThrow(NoSuchElementException::new);
            } catch (SQLException | NoSuchElementException e) {
                e.printStackTrace();
            }
            setInfo(this.log);
            EventQueue.invokeLater(() -> UIManager.getInstance().getUserBorrowPanel().refreshData());
        });
    }

    public void onShow(BorrowLog log) {
        this.log = log;
        setInfo(this.log);
        setVisible(true);
    }

    private void setInfo(BorrowLog log) {
        idValue.setText(String.valueOf(log.getId()));
        bookInputField.setText(String.valueOf(log.getBookId()));
        startTimeShow.setText(BorrowLog.TIME_FORMATTER.format(log.getStartTime()));
        startTime = log.getStartTime();
        endTimeShow.setText(BorrowLog.TIME_FORMATTER.format(log.getEndTime()));
        endTime = log.getEndTime();
        isReturnBox.setSelected(log.isReturn());
    }
}
