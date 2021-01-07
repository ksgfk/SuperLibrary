package cn.edu.jmu.chengyi.superlibrary;

import cn.edu.jmu.chengyi.superlibrary.ui.WindowsControl;

import java.sql.SQLException;

public class Main {
    static {
        DbManager.getInstance().createConnect("root", "123456");
    }

    public static void main(String[] args) throws SQLException {
        WindowsControl.Login();
    }
}
