package cn.edu.jmu.chengyi.superlibrary;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbManager {
	public static final String DB_NAME = "library";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

	public static final String TABLE_NAME = "books";
	public static final String TAG_ID = "id";
	public static final String TAG_TYPE = "type";
	public static final String TAG_NAME = "name";
	public static final String TAG_COUNT = "count";
	public static final String TAG_AUTHOR = "author";
	public static final String TAG_PRICE = "price";
	public static final String TAG_PAGE = "page";

	public static final String SQL_ADD_BOOK = String.format("insert into %s (%s,%s,%s,%s,%s,%s) values (?,?,?,?,?,?)",
			TABLE_NAME, TAG_TYPE, TAG_NAME, TAG_COUNT, TAG_AUTHOR, TAG_PRICE, TAG_PAGE);

	public static String sqlRemove(String tagName) {
		return String.format("delete from %s where %s=?", TABLE_NAME, TAG_ID);
	}

	public static String sqlRemove(String tagName1, String tagName2) {
		return String.format("delete from %s where %s=? and %s=?", TABLE_NAME, tagName1, tagName2);
	}

	public static String sqlSelect(String keyName) {
		return String.format("select * from %s where %s=?", TABLE_NAME, keyName);
	}

	private static class Instance {
		private static DbManager instance = new DbManager();
	}

	private DbManager() {
		try {
			Class.forName(MYSQL_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("Can't load mysql driver.Class name:" + MYSQL_DRIVER);
			System.exit(-1);
		}
	}

	public static DbManager getInstance() {
		return Instance.instance;
	}

	private Connection connect;

	public void createConnect(String username, String password) {
		if (connect != null)
			throw new IllegalStateException("DB Connect has be created");
		try {
			connect = DriverManager.getConnection(DB_URL, username, password);
			System.out.println(String.format("Connected! User:%s", username));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeConnect() {
		if (connect == null)
			throw new IllegalStateException("DB Connect hasn't be created");
		try {
			connect.close();
			System.out.println("Connect closed.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addBook(BookType type, String name, int count, String author, BigDecimal price, int page)
			throws SQLException {
		PreparedStatement prep = connect.prepareStatement(SQL_ADD_BOOK);
		prep.setString(1, type.name());
		prep.setString(2, name);
		prep.setInt(3, count);
		prep.setString(4, author);
		prep.setBigDecimal(5, price);
		prep.setInt(6, page);
		boolean result = prep.execute();
		if (result) {
			throw new IllegalArgumentException("unknown error");
		}
	}

	public void removeBook(int id) throws SQLException {
		PreparedStatement prep = connect.prepareStatement(sqlRemove(TAG_ID));
		prep.setInt(1, id);
		removeBook(prep);
	}

	public void removeBook(String name) throws SQLException {
		PreparedStatement prep = connect.prepareStatement(sqlRemove(TAG_NAME));
		prep.setString(1, name);
		removeBook(prep);
	}

	public void removeBook(String name, String author) throws SQLException {
		PreparedStatement prep = connect.prepareStatement(sqlRemove(TAG_NAME, TAG_AUTHOR));
		prep.setString(1, name);
		prep.setString(2, author);
		removeBook(prep);
	}

	private void removeBook(PreparedStatement prep) throws SQLException {
		boolean result = prep.execute();
		if (result) {
			throw new IllegalArgumentException("unknown error");
		}
	}

	public Optional<Book> getBook(int id) throws SQLException {
		PreparedStatement prep = connect.prepareStatement(sqlSelect(TAG_ID));
		prep.setInt(1, id);
		List<Book> result = getBook(prep);
		if (result.size() != 1)
			return Optional.empty();
		return Optional.of(result.get(0));
	}

	public List<Book> getBooksWithName(String name) throws SQLException {
		PreparedStatement prep = connect.prepareStatement(sqlSelect(TAG_NAME));
		prep.setString(1, name);
		return getBook(prep);
	}

	public List<Book> getBooksWithAuthor(String author) throws SQLException {
		PreparedStatement prep = connect.prepareStatement(sqlSelect(TAG_AUTHOR));
		prep.setString(1, author);
		return getBook(prep);
	}

	private List<Book> getBook(PreparedStatement prep) throws SQLException {
		ResultSet result = prep.executeQuery();
		List<Book> books = new ArrayList<>();
		while (result.next()) {
			int id = result.getInt(TAG_ID);
			String type = result.getString(TAG_TYPE);
			String name = result.getString(TAG_NAME);
			int count = result.getInt(TAG_COUNT);
			String author = result.getString(TAG_AUTHOR);
			BigDecimal price = result.getBigDecimal(TAG_PRICE);
			int page = result.getInt(TAG_PAGE);
			books.add(new Book(id, Enum.valueOf(BookType.class, type), name, count, author, price, page));
		}
		return books;
	}
}
