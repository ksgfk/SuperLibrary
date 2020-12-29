package cn.edu.jmu.chengyi.superlibrary;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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

	public static final String USER_TABLE = "users";
	public static final String TAG_PWD = "password";
	public static final String TAG_PMS = "permission";

	public static final String SQL_ADD_BOOK = String.format("insert into %s (%s,%s,%s,%s,%s,%s) values (?,?,?,?,?,?)",
			BOOK_TABLE, TAG_TYPE, TAG_NAME, TAG_COUNT, TAG_AUTHOR, TAG_PRICE, TAG_PAGE);

	public static final String SQL_ADD_USER = String.format("insert into %s (%s,%s,%s) values(?,?,?)", USER_TABLE,
			TAG_NAME, TAG_PWD, TAG_PMS);

	public static String sqlRemove(String table, String tagName) {
		return String.format("delete from %s where %s=?", table, TAG_ID);
	}

	public static String sqlRemove(String table, String tagName1, String tagName2) {
		return String.format("delete from %s where %s=? and %s=?", table, tagName1, tagName2);
	}

	public static String sqlSelect(String table, String keyName) {
		return String.format("select * from %s where %s=?", table, keyName);
	}

	public static String sqlUpdate(String table, String keyName) {
		return String.format("update %s set %s=? where id=?", table, keyName);
	}

	public static String SHA1(String str) {
		try {
			byte[] raw = str.getBytes("utf-8");
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] res = md.digest(raw);
			StringBuilder sb = new StringBuilder();
			for (byte b : res) {
				String hex = Integer.toHexString(b & 0xFF);
				if (hex.length() == 1) {
					sb.append("0");
				}
				sb.append(hex);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static class Instance {
		private static DbManager instance = new DbManager();
	}

	public static DbManager getInstance() {
		return Instance.instance;
	}

	private Connection connect;
	private PreparedStatement addBook;
	private PreparedStatement rmBookId;
	private PreparedStatement rmBookName;
	private PreparedStatement rmBookNameAuth;
	private PreparedStatement getBookId;
	private PreparedStatement getBookName;
	private PreparedStatement getBookAuth;
	private PreparedStatement getUsrId;
	private PreparedStatement getUsrName;
	private PreparedStatement addUsr;
	private PreparedStatement rmUserId;

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
		Clear();
	}

	private void Clear() {
		addBook = null;
		rmBookId = null;
		rmBookName = null;
		rmBookNameAuth = null;
		getBookId = null;
		getBookName = null;
		getBookAuth = null;
		getUsrId = null;
		getUsrName = null;
		addUsr = null;
		rmUserId = null;
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
		addBook = checkStatement(addBook, () -> SQL_ADD_BOOK);
		addBook.setString(1, type.name());
		addBook.setString(2, name);
		addBook.setInt(3, count);
		addBook.setString(4, author);
		addBook.setBigDecimal(5, price);
		addBook.setInt(6, page);
		boolean result = addBook.execute();
		if (result) {
			throw new IllegalStateException("unknown error");
		}
	}

	public void removeBook(int id) throws SQLException {
		rmBookId = checkStatement(rmBookId, () -> sqlRemove(BOOK_TABLE, TAG_ID));
		rmBookId.setInt(1, id);
		removeBook(rmBookId);
	}

	public void removeBook(String name) throws SQLException {
		rmBookName = checkStatement(rmBookName, () -> sqlRemove(BOOK_TABLE, TAG_NAME));
		rmBookName.setString(1, name);
		removeBook(rmBookName);
	}

	public void removeBook(String name, String author) throws SQLException {
		rmBookNameAuth = checkStatement(rmBookName, () -> sqlRemove(BOOK_TABLE, TAG_NAME, TAG_AUTHOR));
		rmBookNameAuth.setString(1, name);
		rmBookNameAuth.setString(2, author);
		removeBook(rmBookNameAuth);
	}

	private void removeBook(PreparedStatement prep) throws SQLException {
		boolean result = prep.execute();
		if (result) {
			throw new IllegalStateException("unknown error");
		}
	}

	public Optional<Book> getBook(int id) throws SQLException {
		getBookId = checkStatement(getBookId, () -> sqlSelect(BOOK_TABLE, TAG_ID));
		getBookId.setInt(1, id);
		List<Book> result = getBook(getBookId);
		if (result.size() != 1)
			return Optional.empty();
		return Optional.of(result.get(0));
	}

	public List<Book> getBooksWithName(String name) throws SQLException {
		getBookName = checkStatement(getBookName, () -> sqlSelect(BOOK_TABLE, TAG_NAME));
		getBookName.setString(1, name);
		return getBook(getBookName);
	}

	public List<Book> getBooksWithAuthor(String author) throws SQLException {
		getBookAuth = checkStatement(getBookAuth, () -> sqlSelect(BOOK_TABLE, TAG_AUTHOR));
		getBookAuth.setString(1, author);
		return getBook(getBookAuth);
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

	public Book setBook(Book book, String tag, Object value) throws SQLException {
		if (tag.equals(TAG_ID))
			throw new IllegalArgumentException("Can't update id");
		PreparedStatement prep = connect.prepareStatement(sqlUpdate(BOOK_TABLE, tag));
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
		return getBook(book.getId()).get();
	}

	public boolean hasUser(int id) throws SQLException {
		return getUser(id).isPresent();
	}

	public boolean hasUser(String name) throws SQLException {
		try {
			Optional<User> u = getUser(name);
			return u.isPresent();
		} catch (IllegalArgumentException e) {
			return true;
		}
	}

	public Optional<User> getUser(int id) throws SQLException {
		getUsrId = checkStatement(getUsrId, () -> sqlSelect(USER_TABLE, TAG_ID));
		getUsrId.setInt(1, id);
		return getUser(getUsrName);
	}

	public Optional<User> getUser(String name) throws SQLException {
		getUsrName = checkStatement(getUsrName, () -> sqlSelect(USER_TABLE, TAG_NAME));
		getUsrName.setString(1, name);
		ResultSet set = getUsrName.executeQuery();
		if (!set.first())
			return Optional.empty();
		User u = new User(set.getInt(1), set.getString(2), set.getString(3),
				Enum.valueOf(UserPermission.class, set.getString(4)));
		if (set.next())
			throw new IllegalArgumentException("repeat username:" + name + "   maybe bug");
		return Optional.of(u);
	}

	private Optional<User> getUser(PreparedStatement sql) throws SQLException {
		ResultSet set = sql.executeQuery();
		if (!set.first())
			return Optional.empty();
		return Optional.of(new User(set.getInt(1), set.getString(2), set.getString(3),
				Enum.valueOf(UserPermission.class, set.getString(4))));
	}

	public boolean addUser(String name, String password, Function<String, String> encry) throws SQLException {
		if (hasUser(name))
			return false;
		String cry = encry.apply(password);
		if (cry == null)
			return false;
		addUsr = checkStatement(addUsr, () -> SQL_ADD_USER);
		addUsr.setString(1, name);
		addUsr.setString(2, cry);
		addUsr.setString(3, UserPermission.USER.name());
		boolean result = addUsr.execute();
		if (result) {
			throw new IllegalStateException("unknown error");
		}
		return true;
	}

	public void removeUser(User user) throws SQLException {
		User u = getUser(user.getId()).get();
		if (u.getPermission() == UserPermission.ADMIN) {
			throw new IllegalArgumentException("You don't have permission to remove admin");
		}
		rmUserId = checkStatement(rmUserId, () -> sqlRemove(USER_TABLE, TAG_ID));
		rmUserId.setInt(1, u.getId());
		rmUserId.execute();
	}

	public void removeUser(int id) throws SQLException {
		Optional<User> u = getUser(id);
		if (!u.isPresent())
			throw new IllegalArgumentException("no user id" + id);
		removeUser(u.get());
	}

	public void removeUser(String name) throws SQLException {
		Optional<User> u = getUser(name);
		if (!u.isPresent())
			throw new IllegalArgumentException("no user id" + name);
		removeUser(u.get());
	}
}
