package cn.edu.jmu.chengyi.superlibrary.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class JTimeChooser extends JDialog implements ActionListener {
    private static final int DEFAULT_WIDTH = 405;
    private static final int DEFAULT_HEIGHT = 387;
    private int showYears = 100;
    private JButton confirm = null;
    private JButton lastMonth = null;
    private JButton nextMonth = null;
    private JButton button = null;
    private JComboBox comboBox1 = null;
    private JComboBox comboBox2 = null;
    private Calendar calendar = null;
    private String[] years = null;
    private String[] months = null;
    private int year1, month1, day1;
    private int hour, minute, second;
    private JPanel panel = null;
    private String tits[] = {"日", "一", "二", "三", "四", "五", "六"};
    private JComboBox comboBox3 = null;
    private JComboBox comboBox4 = null;
    private JComboBox comboBox5 = null;
    private int rowlens = 5;
    private String title = "选择时间";
    private Point location = null;
    private boolean flag;

    public JTimeChooser(Frame parent) {
        super(parent, true);
        this.setTitle(title);
        // init
        this.initDatas();
        this.setResizable(false);
    }

    private void initDatas() {
        this.calendar = Calendar.getInstance();
        this.year1 = this.calendar.get(Calendar.YEAR);
        this.month1 = this.calendar.get(Calendar.MONTH);
        this.day1 = this.calendar.get(Calendar.DAY_OF_MONTH);
        this.hour = this.calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = this.calendar.get(Calendar.MINUTE);
        this.second = this.calendar.get(Calendar.SECOND);
        this.years = new String[showYears];
        this.months = new String[12];
        for (int i = 0; i < this.months.length; i++) {
            this.months[i] = " " + formatDay(i + 1);
        }
        int start = this.year1 - showYears / 2;
        for (int i = start; i < start + showYears; i++) {
            this.years[i - start] = String.valueOf(i);
        }
        this.calendar.set(Calendar.HOUR_OF_DAY, 0);
        this.calendar.set(Calendar.MINUTE, 0);
        this.calendar.set(Calendar.SECOND, 0);
    }

    private Dimension getStartDimension(int width, int height) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dim.width = dim.width / 2 - width / 2;
        dim.height = dim.height / 2 - height / 2;
        return dim;
    }

    public Calendar showTimeDialog() {
        this.initComponents();
        return this.calendar;
    }

    private void initComponents() {
        getContentPane().setLayout(new BorderLayout());
        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel3.setBackground(Color.WHITE);
        showNorthPanel(panel3);
        getContentPane().add(panel3, BorderLayout.NORTH);
        getContentPane().add(printCalendar(), BorderLayout.CENTER);
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.WHITE);
        panel2.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.showSouthPanel(panel2);
        getContentPane().add(panel2, BorderLayout.SOUTH);
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        if (this.location == null) {
            Dimension dim = getStartDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            setLocation(dim.width, dim.height);
        } else {
            setLocation(this.location);
        }
        this.setVisible(true);
    }

    private void showNorthPanel(JPanel panel) {
        this.lastMonth = new JButton("上一月");
        this.lastMonth.setToolTipText("上一月");
        this.lastMonth.addActionListener(this);
        panel.add(this.lastMonth);
        this.comboBox1 = new JComboBox(this.years);
        this.comboBox1.setSelectedItem(String.valueOf(year1));
        this.comboBox1.setToolTipText("年份");
        this.comboBox1.setMaximumRowCount(rowlens);
        this.comboBox1.setActionCommand("year");
        this.comboBox1.addActionListener(this);
        panel.add(this.comboBox1);
        this.comboBox2 = new JComboBox(this.months);
        this.comboBox2.setSelectedItem(" " + formatDay(month1 + 1));
        this.comboBox2.setToolTipText("月份");
        this.comboBox2.setMaximumRowCount(rowlens);
        this.comboBox2.addActionListener(this);
        this.comboBox2.setActionCommand("month");
        panel.add(this.comboBox2);
        this.nextMonth = new JButton("下一月");
        this.nextMonth.setToolTipText("下一月");
        this.nextMonth.addActionListener(this);
        panel.add(this.nextMonth);
    }

    private void showSouthPanel(JPanel panel) {
        JPanel panel_23 = new JPanel();
        panel_23.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel label21 = new JLabel("时间: ");
        label21.setForeground(Color.black);
        panel_23.add(label21);
        this.comboBox3 = new JComboBox(this.getHours());
        this.comboBox3.setMaximumRowCount(rowlens);
        this.comboBox3.setToolTipText("时");
        this.comboBox3.setActionCommand("hour");
        this.comboBox3.addActionListener(this);
        this.comboBox3.setSelectedItem(hour);
        panel_23.add(this.comboBox3);
        JLabel label22 = new JLabel("时 ");
        label22.setForeground(Color.black);
        panel_23.add(label22);
        this.comboBox4 = new JComboBox(this.getMins());
        this.comboBox4.setToolTipText("分");
        this.comboBox4.setMaximumRowCount(rowlens);
        this.comboBox4.setActionCommand("minute");
        this.comboBox4.addActionListener(this);
        this.comboBox4.setSelectedItem(minute);
        panel_23.add(this.comboBox4);
        JLabel label23 = new JLabel("分 ");
        label23.setForeground(Color.black);
        panel_23.add(label23);
        this.comboBox5 = new JComboBox(this.getMins());
        this.comboBox5.setToolTipText("秒");
        this.comboBox5.setActionCommand("second");
        this.comboBox5.addActionListener(this);
        this.comboBox5.setMaximumRowCount(rowlens);
        this.comboBox5.setSelectedItem(second);
        panel_23.add(this.comboBox5);
        JLabel label24 = new JLabel("秒");
        label24.setForeground(Color.black);
        panel_23.add(label24);
        panel.add(panel_23);
        this.confirm = new JButton("确定");
        this.confirm.setToolTipText("确定");
        this.confirm.addActionListener(this);
        panel.add(confirm);
    }

    private Object[] getHours() {
        Object[] hs = new Object[24];
        for (int i = 0; i < hs.length; i++) {
            hs[i] = i;
        }
        return hs;
    }

    private Object[] getMins() {
        Object[] hs = new Object[60];
        for (int i = 0; i < hs.length; i++) {
            hs[i] = i;
        }
        return hs;
    }

    private JPanel printCalendar() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(7, 7, 0, 0));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        int year2 = calendar.get(Calendar.YEAR);
        int month2 = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        JButton b = null;
        for (int i = 0; i < tits.length; i++) {
            b = new JButton(tits[i]);
            b.setForeground(new Color(100, 0, 102));
            b.setEnabled(false);
            panel.add(b);
        }
        int count = 0;
        for (int i = Calendar.SUNDAY; i < weekDay; i++) {
            b = new JButton(" ");
            b.setEnabled(false);
            panel.add(b);
            count++;
        }
        int currday = 0;
        String dayStr = null;
        do {
            currday = calendar.get(Calendar.DAY_OF_MONTH);
            dayStr = formatDay(currday);
            if (currday == day1 && month1 == month2 && year1 == year2) {
                b = new JButton("[" + dayStr + "]");
                b.setForeground(Color.red);
            } else {
                b = new JButton(dayStr);
                b.setForeground(Color.black);
            }
            count++;
            b.setToolTipText(year2 + "/" + formatDay(month2 + 1) + "/" + dayStr);
            b.setBorder(BorderFactory.createEtchedBorder());
            b.addActionListener(this);
            panel.add(b);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (calendar.get(Calendar.MONTH) == month2);
        this.calendar.add(Calendar.MONTH, -1);
        if (!flag) {
            this.calendar.set(Calendar.DAY_OF_MONTH, this.day1);
            flag = true;
        }
        for (int i = count; i < 42; i++) {
            b = new JButton(" ");
            b.setEnabled(false);
            panel.add(b);
        }
        return panel;
    }

    private String formatDay(int day) {
        if (day < 10) {
            return "0" + day;
        }
        return String.valueOf(day);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ("下一月".equals(command)) {
            this.calendar.add(Calendar.MONTH, 1);
            int year5 = calendar.get(Calendar.YEAR);
            int maxYear = this.year1 + this.showYears / 2 - 1;
            if (year5 > maxYear) {
                this.calendar.add(Calendar.MONTH, -1);
                return;
            }
            int month5 = calendar.get(Calendar.MONTH) + 1;
            this.comboBox1.setSelectedItem(String.valueOf(year5));
            this.comboBox2.setSelectedItem(" " + this.formatDay(month5));
            this.updatePanel();
        } else if ("上一月".equals(command)) {
            this.calendar.add(Calendar.MONTH, -1);
            int year5 = calendar.get(Calendar.YEAR);
            int minYear = this.year1 - this.showYears / 2;
            if (year5 < minYear) {
                this.calendar.add(Calendar.MONTH, 1);
                return;
            }
            int month5 = calendar.get(Calendar.MONTH) + 1;
            this.comboBox1.setSelectedItem(String.valueOf(year5));
            this.comboBox2.setSelectedItem(" " + this.formatDay(month5));
            this.updatePanel();
        } else if ("确定".equals(command)) {
            this.dispose();
        } else if (command.matches("^\\d+$")) {
            JButton b = (JButton) e.getSource();
            if (this.button == null) {
                this.button = b;
            } else {
                this.button.setForeground(Color.black);
                this.button.setFont(b.getFont());
                this.button = b;
            }
            b.setForeground(new Color(0XFFD700));
            b.setFont(button.getFont().deriveFont(Font.BOLD));
            int day9 = Integer.parseInt(command);
            this.calendar.set(Calendar.DAY_OF_MONTH, day9);
        } else if (command.startsWith("[")) {
            JButton b = (JButton) e.getSource();
            if (this.button == null) {
                this.button = b;
            } else {
                this.button.setForeground(Color.black);
                this.button.setFont(b.getFont());
                this.button = b;
            }
            b.setForeground(new Color(0XFFD700));
            b.setFont(button.getFont().deriveFont(Font.BOLD));
            this.calendar.set(Calendar.DAY_OF_MONTH, this.day1);
        } else if ("hour".equalsIgnoreCase(command)) {
            int value = Integer.parseInt(this.comboBox3.getSelectedItem().toString().trim());
            this.calendar.set(Calendar.HOUR_OF_DAY, value);
        } else if ("minute".equalsIgnoreCase(command)) {
            int value = Integer.parseInt(this.comboBox4.getSelectedItem().toString().trim());
            this.calendar.set(Calendar.MINUTE, value);
        } else if ("second".equalsIgnoreCase(command)) {
            int value = Integer.parseInt(this.comboBox5.getSelectedItem().toString().trim());
            this.calendar.set(Calendar.SECOND, value);
        } else if ("year".equalsIgnoreCase(command)) {
            int value = Integer.parseInt(this.comboBox1.getSelectedItem().toString().trim());
            this.calendar.set(Calendar.YEAR, value);
            this.updatePanel();
        } else if ("month".equalsIgnoreCase(command)) {
            int value = Integer.parseInt(this.comboBox2.getSelectedItem().toString().trim());
            this.calendar.set(Calendar.MONTH, value - 1);
            this.updatePanel();
        }
    }

    private void updatePanel() {
        this.remove(this.panel);
        getContentPane().add(this.printCalendar(), BorderLayout.CENTER);
        this.validate();
        this.repaint();
    }
}