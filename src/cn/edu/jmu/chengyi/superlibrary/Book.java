package cn.edu.jmu.chengyi.superlibrary;

import java.math.BigDecimal;

public class Book {
	private int id;
	private BookType type;
	private String name;
	private int count;
	private String author;
	private BigDecimal price;
	private int page;

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
}
