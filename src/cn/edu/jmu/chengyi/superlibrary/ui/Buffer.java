package cn.edu.jmu.chengyi.superlibrary.ui;

import java.util.List;

import cn.edu.jmu.chengyi.superlibrary.BookType;

public class Buffer {
    public static List<String> BooksType;

    public static void main(String[] args) {
        BooksType.add(BookType.Education.name());
        BooksType.add(BookType.History.name());
        BooksType.add(BookType.Literature.name());
        BooksType.add(BookType.Science.name());
    }
}
