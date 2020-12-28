package cn.edu.jmu.chengyi.superlibrary;

import java.sql.SQLException;
import java.util.Optional;

public class Main {
	public static void main(String[] args) throws SQLException {
		DbManager.getInstance().createConnect("root", "123456");
		DbManager manager = DbManager.getInstance();

//		manager.addBook(BookType.Science, "下北泽", 1, "野兽", new BigDecimal("114514.1"), 1919810);
		Optional<Book> b = manager.getBook(10);
		System.out.println(b);
		
		manager.closeConnect();
	}
}
