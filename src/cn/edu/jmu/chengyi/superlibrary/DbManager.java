package cn.edu.jmu.chengyi.superlibrary;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

    public static final String BORROW_TABLE = "borrowLog";
    public static final String TAG_BORROWER_ID = "borrowerId";
    public static final String TAG_BOOK_ID = "bookId";
    public static final String TAG_START_TIME = "startTime";
    public static final String TAG_END_TIME = "endTime";
    public static final String TAG_IS_RETURN = "isReturn";

    public static final String SQL_ADD_BOOK = String.format("insert into %s (%s,%s,%s,%s,%s,%s) values (?,?,?,?,?,?)",
            BOOK_TABLE, TAG_TYPE, TAG_NAME, TAG_COUNT, TAG_AUTHOR, TAG_PRICE, TAG_PAGE);

    public static final String SQL_ADD_USER = String.format("insert into %s (%s,%s,%s) values(?,?,?)", USER_TABLE,
            TAG_NAME, TAG_PWD, TAG_PMS);

    public static final String SQL_ADD_LOG = String.format("insert into %s (%s,%s,%s,%s) values (?,?,?,?)",
            BORROW_TABLE, TAG_BORROWER_ID, TAG_BOOK_ID, TAG_START_TIME, TAG_END_TIME);

    public static String sqlRemove(String table, String tagName) {
        return String.format("delete from %s where %s=?", table, tagName);
    }

    public static String sqlRemove(String table, String tagName1, String tagName2) {
        return String.format("delete from %s where %s=? and %s=?", table, tagName1, tagName2);
    }

    public static String sqlSelect(String table, String keyName) {
        return String.format("select * from %s where %s=?", table, keyName);
    }

    public static String sqlSelect(String table, String keyName1, String keyName2) {
        return String.format("select * from %s where %s=? and %s=?", table, keyName1, keyName2);
    }

    public static String sqlUpdate(String table, String keyName) {
        return String.format("update %s set %s=? where id=?", table, keyName);
    }

    public static String SHA1(String str) {
        try {
            byte[] raw = str.getBytes(StandardCharsets.UTF_8);
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
        private static final DbManager instance = new DbManager();
    }

    public static DbManager getInstance() {
        return Instance.instance;
    }

    private Connection connect;
    private PreparedStatement addBook;
    private PreparedStatement rmBookId;
    private PreparedStatement rmBookName;
    private PreparedStatement rmBookNameAuth;
    private PreparedStatement getBooks;
    private PreparedStatement getBookId;
    private PreparedStatement getBookName;
    private PreparedStatement getBookAuth;
    private PreparedStatement getBookNameAuth;
    private PreparedStatement setBook;
    private PreparedStatement getUsr;
    private PreparedStatement getUsrId;
    private PreparedStatement getUsrName;
    private PreparedStatement addUsr;
    private PreparedStatement rmUserId;
    private PreparedStatement setUsr;
    private PreparedStatement addLog;
    private PreparedStatement getLogId;
    private PreparedStatement getLogBk;
    private PreparedStatement setLogRt;
    private PreparedStatement setLog;
    private PreparedStatement addLogUn;

    private DbManager() {
        try {
            Class.forName(MYSQL_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't load mysql driver.Class name:" + MYSQL_DRIVER);
            System.exit(-1);
        }
    }

    public synchronized void createConnect(String username, String password) {
        if (connect != null)
            throw new IllegalStateException("DB Connect has be created");
        try {
            connect = DriverManager.getConnection(DB_URL, username, password);
            System.out.printf("Connected! User:%s%n", username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void closeConnect() {
        if (connect == null)
            throw new IllegalStateException("DB Connect hasn't be created");
        try {
            connect.close();
            System.out.println("Connect closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private synchronized PreparedStatement checkStatement(PreparedStatement prep, Supplier<String> getSql) throws SQLException {
        if (connect == null) throw new IllegalStateException("未与SQL建立连接");
        if (prep == null || prep.isClosed()) {
            return connect.prepareStatement(getSql.get());
        } else {
            return prep;
        }
    }

    public synchronized void addBook(BookType type, String name, int count, String author, BigDecimal price, int page)
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

    public synchronized void removeBook(int id) throws SQLException {
        rmBookId = checkStatement(rmBookId, () -> sqlRemove(BOOK_TABLE, TAG_ID));
        rmBookId.setInt(1, id);
        removeBook(rmBookId);
    }

    public synchronized void removeBook(String name) throws SQLException {
        rmBookName = checkStatement(rmBookName, () -> sqlRemove(BOOK_TABLE, TAG_NAME));
        rmBookName.setString(1, name);
        removeBook(rmBookName);
    }

    public synchronized void removeBook(String name, String author) throws SQLException {
        rmBookNameAuth = checkStatement(rmBookNameAuth, () -> sqlRemove(BOOK_TABLE, TAG_NAME, TAG_AUTHOR));
        rmBookNameAuth.setString(1, name);
        rmBookNameAuth.setString(2, author);
        removeBook(rmBookNameAuth);
    }

    private synchronized void removeBook(PreparedStatement prep) throws SQLException {
        boolean result = prep.execute();
        if (result) {
            throw new IllegalStateException("unknown error");
        }
    }

    public synchronized boolean hasBook(int id) throws SQLException {
        return getBook(id).isPresent();
    }

    public synchronized Optional<Book> getBook(int id) throws SQLException {
        getBookId = checkStatement(getBookId, () -> sqlSelect(BOOK_TABLE, TAG_ID));
        getBookId.setInt(1, id);
        List<Book> result = getBook(getBookId);
        if (result.size() != 1)
            return Optional.empty();
        return Optional.of(result.get(0));
    }

    public synchronized List<Book> getBooksWithName(String name) throws SQLException {
        getBookName = checkStatement(getBookName, () -> sqlSelect(BOOK_TABLE, TAG_NAME));
        getBookName.setString(1, name);
        return getBook(getBookName);
    }

    public synchronized List<Book> getBooksWithAuthor(String author) throws SQLException {
        getBookAuth = checkStatement(getBookAuth, () -> sqlSelect(BOOK_TABLE, TAG_AUTHOR));
        getBookAuth.setString(1, author);
        return getBook(getBookAuth);
    }

    public synchronized List<Book> getBooksWithNameAndAuthor(String name, String author) throws SQLException {
        getBookNameAuth = checkStatement(getBookNameAuth, () -> sqlSelect(BOOK_TABLE, TAG_NAME, TAG_AUTHOR));
        getBookNameAuth.setString(1, name);
        getBookNameAuth.setString(2, author);
        return getBook(getBookNameAuth);
    }

    public synchronized List<Book> getAllBooks() throws SQLException {
        getBooks = checkStatement(getBooks, () -> "select * from " + BOOK_TABLE);
        return getBook(getBooks);
    }

    private synchronized List<Book> getBook(PreparedStatement prep) throws SQLException {
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

    @Deprecated
    public synchronized Book setBook(Book book, String tag, Object value) throws SQLException {
        if (tag.equals(TAG_ID)) throw new IllegalArgumentException("Can't update id");
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
        return getBook(book.getId()).orElseThrow();
    }

    public synchronized void setBook(Book book, BookProperty property) throws SQLException {
        setBook = checkStatement(setBook, () -> String.format("update %s set %s=?,%s=?,%s=?,%s=?,%s=?,%s=? where id=?",
                BOOK_TABLE,
                TAG_TYPE,
                TAG_NAME,
                TAG_COUNT,
                TAG_AUTHOR,
                TAG_PRICE,
                TAG_PAGE));
        setBook.setString(1, property.getType().name());
        setBook.setString(2, property.getName());
        setBook.setInt(3, property.getCount());
        setBook.setString(4, property.getAuthor());
        setBook.setBigDecimal(5, property.getPrice());
        setBook.setInt(6, property.getPage());
        setBook.setInt(7, book.getId());
        setBook.execute();
    }

    public synchronized boolean hasUser(int id) throws SQLException {
        return getUser(id).isPresent();
    }

    public synchronized boolean hasUser(String name) throws SQLException {
        try {
            Optional<User> u = getUser(name);
            return u.isPresent();
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    public synchronized List<User> getAllUser() throws SQLException {
        getUsr = checkStatement(getUsr, () -> "select * from " + USER_TABLE);
        ResultSet set = getUsr.executeQuery();
        List<User> result = new ArrayList<>();
        while (set.next()) {
            result.add(new User(set.getInt(1), set.getString(2), set.getString(3),
                    Enum.valueOf(UserPermission.class, set.getString(4))));
        }
        return result;
    }

    public synchronized Optional<User> getUser(int id) throws SQLException {
        getUsrId = checkStatement(getUsrId, () -> sqlSelect(USER_TABLE, TAG_ID));
        getUsrId.setInt(1, id);
        return getUser(getUsrId);
    }

    public synchronized Optional<User> getUser(String name) throws SQLException {
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

    private synchronized Optional<User> getUser(PreparedStatement sql) throws SQLException {
        ResultSet set = sql.executeQuery();
        if (!set.first())
            return Optional.empty();
        return Optional.of(new User(set.getInt(1), set.getString(2), set.getString(3),
                Enum.valueOf(UserPermission.class, set.getString(4))));
    }

    public synchronized boolean addUser(String name, String password, Function<String, String> encry) throws SQLException {
        if (!name.isEmpty() && hasUser(name)) return false;
        if (password == null) return false;
        String cry = password.isEmpty() ? "" : encry.apply(password);
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

    public synchronized void removeUser(User user) throws SQLException {
        User u = getUser(user.getId()).orElseThrow();
        if (u.getPermission() == UserPermission.ADMIN) {
            throw new IllegalArgumentException("You don't have permission to remove administrators");
        }
        rmUserId = checkStatement(rmUserId, () -> sqlRemove(USER_TABLE, TAG_ID));
        rmUserId.setInt(1, u.getId());
        rmUserId.execute();
    }

    public synchronized void removeUser(int id) throws SQLException {
        Optional<User> u = getUser(id);
        if (u.isEmpty()) throw new IllegalArgumentException("no user id" + id);
        removeUser(u.get());
    }

    public synchronized void removeUser(String name) throws SQLException {
        Optional<User> u = getUser(name);
        if (u.isEmpty()) throw new IllegalArgumentException("no user id" + name);
        removeUser(u.get());
    }

    public synchronized void setUser(User user, String name, String pwd, UserPermission pms) throws SQLException {
        setUsr = checkStatement(setUsr, () -> String.format("update %s set %s=?,%s=?,%s=? where id=?", USER_TABLE, TAG_NAME, TAG_PWD, TAG_PMS));
        setUsr.setString(1, name);
        setUsr.setString(2, pwd);
        setUsr.setString(3, pms.name());
        setUsr.setInt(4, user.getId());
        setUsr.execute();
    }

    public synchronized void addBorrowLog(Book book, User user, Date returnTime) throws SQLException {
        if (!hasBook(book.getId()) || !hasUser(user.getId())) {
            throw new IllegalArgumentException();
        }
        addLog = checkStatement(addLog, () -> SQL_ADD_LOG);
        addLog.setInt(1, user.getId());
        addLog.setInt(2, book.getId());
        addLog.setString(3, String.valueOf(System.currentTimeMillis()));
        addLog.setString(4, String.valueOf(returnTime.getTime()));
        addLog.execute();
    }

    public synchronized void addBorrowLog(int borrower, int book, Date startTime, Date endTime, boolean isRet) throws SQLException {
        addLogUn = checkStatement(addLogUn, () -> "insert into borrowlog (borrowerId,bookId,startTime,endTime,isReturn) values (?,?,?,?,?)");
        addLogUn.setInt(1, borrower);
        addLogUn.setInt(2, book);
        addLogUn.setString(3, String.valueOf(startTime.getTime()));
        addLogUn.setString(4, String.valueOf(endTime.getTime()));
        addLogUn.setBoolean(5, isRet);
        addLogUn.execute();
    }

    public synchronized void setBorrowLog(BorrowLog log, int borrower, int book, Date startTime, Date endTime, boolean isRet) throws SQLException {
        setLog = checkStatement(setLog, () -> "update borrowlog set borrowerId=?,bookId=?,startTime=?,endTime=?,isReturn=? where id=?");
        setLog.setInt(1, borrower);
        setLog.setInt(2, book);
        setLog.setString(3, String.valueOf(startTime.getTime()));
        setLog.setString(4, String.valueOf(endTime.getTime()));
        setLog.setBoolean(5, isRet);
        setLog.setInt(6, log.getId());
        setLog.execute();
    }

    public synchronized List<BorrowLog> getBorrowLog(User user) throws SQLException {
        getLogId = checkStatement(getLogId, () -> sqlSelect(BORROW_TABLE, TAG_BORROWER_ID));
        getLogId.setInt(1, user.getId());
        return getBorrowLog(getLogId);
    }

    public synchronized List<BorrowLog> getBorrowLog(Book book) throws SQLException {
        getLogBk = checkStatement(getLogBk, () -> sqlSelect(BORROW_TABLE, TAG_BOOK_ID));
        getLogBk.setInt(1, book.getId());
        return getBorrowLog(getLogBk);
    }

    private PreparedStatement getLogLogId;

    public synchronized Optional<BorrowLog> getBorrowLog(int id) throws SQLException {
        getLogLogId = checkStatement(getLogLogId, () -> sqlSelect(BORROW_TABLE, TAG_ID));
        getLogLogId.setInt(1, id);
        List<BorrowLog> logs = getBorrowLog(getLogLogId);
        return logs.size() == 1 ? Optional.of(logs.get(0)) : Optional.empty();
    }

    private synchronized List<BorrowLog> getBorrowLog(PreparedStatement sql) throws SQLException {
        ResultSet set = sql.executeQuery();
        List<BorrowLog> result = new ArrayList<>();
        while (set.next()) {
            result.add(new BorrowLog(set.getInt(1), set.getInt(2), set.getInt(3), set.getString(4), set.getString(5),
                    set.getBoolean(6)));
        }
        return result;
    }

    public synchronized void setBookReturn(int id, boolean isReturn) throws SQLException {
        setLogRt = checkStatement(setLogRt, () -> sqlUpdate(BORROW_TABLE, TAG_IS_RETURN));
        setLogRt.setBoolean(1, isReturn);
        setLogRt.setInt(2, id);
        setLogRt.execute();
    }
}
