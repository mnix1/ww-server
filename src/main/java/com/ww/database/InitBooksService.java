package com.ww.database;

import com.ww.model.constant.book.BookType;
import com.ww.model.entity.outside.book.Book;
import com.ww.repository.outside.book.BookRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Service
public class InitBooksService {

    @Autowired
    private BookRepository bookRepository;

    public void initBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(BookType.LEAFLET, 1, 0, 1L, 0L, 0L, true, false, 1L, 0L));
        books.add(new Book(BookType.FAIRY_TALE, 1, 0, 0L, 0L, 1L, true, false, 1L, 0L));
        books.add(new Book(BookType.TV_GUIDE, 2, 1, 3L, 0L, 0L, true, false, 2L, 0L));
        books.add(new Book(BookType.COLORFUL_MAGAZINE, 2, 1, 2L, 0L, 1L, true, false, 2L, 0L));
        books.add(new Book(BookType.SPORT_MAGAZINE, 2, 1, 1L, 0L, 2L, true, false, 2L, 0L));
        books.add(new Book(BookType.NEWSPAPER, 2, 1, 0L, 0L, 3L, true, false, 2L, 0L));
        books.add(new Book(BookType.ROMANCE_NOVEL, 4, 2, 7L, 0L, 0L, true, false, 4L, 0L));
        books.add(new Book(BookType.USER_MANUAL, 4, 2, 3L, 0L, 4L, true, false, 4L, 0L));
        books.add(new Book(BookType.BIOGRAPHY, 4, 2, 1L, 0L, 6L, true, false, 4L, 0L));
        books.add(new Book(BookType.HISTORICAL_NOVEL, 8, 3, 10L, 0L, 5L, true, false, 8L, 0L));
        books.add(new Book(BookType.CROSSWORD, 8, 3, 7L, 0L, 8L, true, false, 8L, 0L));
        books.add(new Book(BookType.FINANCIAL_STATEMENT, 8, 3, 1L, 0L, 14L, true, false, 8L, 0L));
        books.add(new Book(BookType.WORLD_ATLAS, 12, 4, 10L, 0L, 13L, true, false, 12L, 0L));
        books.add(new Book(BookType.STUDENT_BOOK, 12, 4, 3L, 0L, 20L, true, false, 12L, 0L));
        books.add(new Book(BookType.ENCYCLOPEDIA, 16, 5, 11L, 0L, 20L, true, false, 16L, 0L));
        books.add(new Book(BookType.SCIENCE_ARTICLE, 16, 5, 5L, 0L, 26L, true, false, 16L, 0L));
        books.add(new Book(BookType.MYSTERIOUS_BOOK, 20, 6, 0L, 30L, 9L, false, true, 0L, 100L));
        books.add(new Book(BookType.SECRET_BOOK, 20, 6, 19L, 20L, 0L, false, false, 0L, 0L));
        bookRepository.saveAll(books);
    }

}
