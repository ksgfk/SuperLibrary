package cn.edu.jmu.chengyi.superlibrary;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

public class Main {
	public static void main(String[] args) throws SQLException {
		DbManager.getInstance().createConnect("root", "123456");
		DbManager manager = DbManager.getInstance();

		manager.addBook(BookType.Science, "猫猫头", 1, "???", new BigDecimal("621"), 231);
		Optional<Book> b = manager.getBook(11);
		System.out.println(b);
		
		manager.removeBook(11);
		b = manager.getBook(11);
		System.out.println(b);
		
		b = manager.getBook(10);
		System.out.println(b);
		manager.setBook(b.get(), DbManager.TAG_COUNT, 3);
		b = manager.getBook(10);
		System.out.println(b);
		
		manager.closeConnect();
	}
}
