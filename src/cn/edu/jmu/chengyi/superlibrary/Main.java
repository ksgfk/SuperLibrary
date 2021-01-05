package cn.edu.jmu.chengyi.superlibrary;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import cn.edu.jmu.chengyi.superlibrary.ui.JTimeChooser;
import cn.edu.jmu.chengyi.superlibrary.ui.UIManager;
import cn.edu.jmu.chengyi.superlibrary.ui.WindowsControl;

public class Main {
    static {
        DbManager.getInstance().createConnect("root", "123456");
    }

    public static void main(String[] args) throws SQLException {
        DbManager manager = DbManager.getInstance();

//		manager.addBook(BookType.Science, "猫猫头", 1, "???", new BigDecimal("621"), 231);
//		Optional<Book> b = manager.getBook(11);
//		System.out.println(b);
//		
//		manager.removeBook(11);
//		b = manager.getBook(11);
//		System.out.println(b);
//		
//		b = manager.getBook(10);
//		System.out.println(b);
//		manager.setBook(b.get(), DbManager.TAG_COUNT, 3);
//		b = manager.getBook(10);
//		System.out.println(b);
//		List<Book> b = manager.getBooksWithName("猫猫头");
//		for(Book bk : b) {
//			System.out.println(bk);
//		}

//		manager.addUser("kabi0210", "2333", DbManager::SHA1);
//		System.out.println(DbManager.SHA1("2333"));
//		System.out.println(manager.getUser("kabi0210"));
//		WindowsControl.main(null);

//		WindowsControl.main(null);
		UIManager.getInstance();

//        Book b = new Book(10, BookType.Education, "", 0, "", null, 0);
//        User u = new User(1, "", "", UserPermission.USER);
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
//        c.add(Calendar.MONTH, 3);
//        DbManager.getInstance().addBorrowLog(b, u, c.getTime());

//		manager.closeConnect();
    }
}
