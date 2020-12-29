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
import java.util.function.Supplier;

public class DbManager {
	public static final String DB_NAME = "library";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

	public static final String BOOK_TABLE = "books";
	public static final String TAG_ID = "id";
	public static final String TAG_TYPE = "type";
	public static final String TAG_NAME = "name";
	public static final String TAG_COUNT = "count";
	public static final String TAG_AUTHOR = "author";
	public static final String TAG_PRICE = "price";
	public static final String TAG_PAGE = "page";

	public static final String SQL_ADD_BOOK = String.format("insert into %s (%s,%s,%s,%s,%s,%s) values (?,?,?,?,?,?)",
			BOOK_TABLE, TAG_TYPE, TAG_NAME, TAG_COUNT, TAG_AUTHOR, TAG_PRICE, TAG_PAGE);

	public static String sqlRemove(String tagName) {
		return String.format("delete from %s where %s=?", BOOK_TABLE, TAG_ID);
	}

	public static String sqlRemove(String tagName1, String tagName2) {
		return String.format("delete from %s where %s=? and %s=?", BOOK_TABLE, tagName1, tagName2);
	}

	public static String sqlSelect(String keyName) {
		return String.format("select * from %s where %s=?", BOOK_TABLE, keyName);
	}

	public static String sqlUpdate(String keyName) {
		return String.format("update %s set %s=? where id=?", BOOK_TABLE, keyName);
	}

	private static class Instance {
		private static DbManager instance = new DbManager();
	}

	public static DbManager getInstance() {
		return Instance.instance;
	}

	private Connection connect;
	private PreparedStatement addState;
	private PreparedStatement rmIdState;
	private PreparedStatement rmNameState;
	private PreparedStatement rmNameAuthState;
	private PreparedStatement getIdState;
	private PreparedStatement getNameState;
	private PreparedStatement getAuthState;

	private DbManager() {
		try {
			Class.forName(MYSQL_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("Can't load mysql driver.Class name:" + MYSQL_DRIVER);
			System.exit(-1);
		}
	}

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

	private PreparedStatement checkStatement(PreparedStatement prep, Supplier<String> getSql) throws SQLException {
		if (prep == null || prep.isClosed()) {
			return connect.prepareStatement(getSql.get());
		} else {
			return prep;
		}
	}

	public void addBook(BookType type, String name, int count, String author, BigDecimal price, int page)
			throws SQLException {
		addState = checkStatement(addState, () -> SQL_ADD_BOOK);
		addState.setString(1, type.name());
		addState.setString(2, name);
		addState.setInt(3, count);
		addState.setString(4, author);
		addState.setBigDecimal(5, price);
		addState.setInt(6, page);
		boolean result = addState.execute();
		if (result) {
			throw new IllegalStateException("unknown error");
		}
	}

	public void removeBook(int id) throws SQLException {
		rmIdState = checkStatement(rmIdState, () -> sqlRemove(TAG_ID));
		rmIdState.setInt(1, id);
		removeBook(rmIdState);
	}

	public void removeBook(String name) throws SQLException {
		rmNameState = checkStatement(rmNameState, () -> sqlRemove(TAG_NAME));
		rmNameState.setString(1, name);
		removeBook(rmNameState);
	}

	public void removeBook(String name, String author) throws SQLException {
		rmNameAuthState = checkStatement(rmNameState, () -> sqlRemove(TAG_NAME, TAG_AUTHOR));
		rmNameAuthState.setString(1, name);
		rmNameAuthState.setString(2, author);
		removeBook(rmNameAuthState);
	}

	private void removeBook(PreparedStatement prep) throws SQLException {
		boolean result = prep.execute();
		if (result) {
			throw new IllegalStateException("unknown error");
		}
	}

	public Optional<Book> getBook(int id) throws SQLException {
		getIdState = checkStatement(getIdState, () -> sqlSelect(TAG_ID));
		getIdState.setInt(1, id);
		List<Book> result = getBook(getIdState);
		if (result.size() != 1)
			return Optional.empty();
		return Optional.of(result.get(0));
	}

	public List<Book> getBooksWithName(String name) throws SQLException {
		getNameState = checkStatement(getNameState, () -> sqlSelect(TAG_NAME));
		getNameState.setString(1, name);
		return getBook(getNameState);
	}

	public List<Book> getBooksWithAuthor(String author) throws SQLException {
		getAuthState = checkStatement(getAuthState, () -> sqlSelect(TAG_AUTHOR));
		getAuthState.setString(1, author);
		return getBook(getAuthState);
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

	public void setBook(Book book, String tag, Object value) throws SQLException {
		if (tag.equals(TAG_ID))
			throw new IllegalArgumentException("Can't update id");
		PreparedStatement prep = connect.prepareStatement(sqlUpdate(tag));
		if (value instanceof Integer) {
			prep.setInt(1, (int) value);
		} else if (value instanceof String) {
			prep.setString(1, (String) value);
		} else if (value instanceof BigDecimal) {
			prep.setBigDecimal(1, (BigDecimal) value);
		} else {
			throw new IllegalArgumentException("type of value should be int/String/BigDecimal");
		}
		prep.setInt(2, book.getId());
		int result = prep.executeUpdate();
		if (result != 1) {
			throw new IllegalStateException("unknown error");
		}
	}
}
