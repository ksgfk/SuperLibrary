package cn.edu.jmu.chengyi.superlibrary;

import java.math.BigDecimal;

public final class Book {
    private final int id;
    private final BookType type;
    private final String name;
    private final int count;
    private final String author;
    private final BigDecimal price;
    private final int page;

    Book(int id, BookType type, String name, int count, String author, BigDecimal price, int page) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.count = count;
        this.author = author;
        this.price = price;
        this.page = page;
    }

    public int getId() {
        return id;
    }

    public BookType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getAuthor() {
        return author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", type=" + type + ", name=" + name + ", count=" + count + ", author=" + author
                + ", price=" + price + ", page=" + page + "]";
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public static final Book NoonBook = new Book(1, BookType.Education, "The man who changes the china", 1926, "虫合",
            new BigDecimal("8"), 17);
}
