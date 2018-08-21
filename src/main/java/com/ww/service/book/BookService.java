package com.ww.service.book;

import com.ww.model.entity.book.Book;
import com.ww.repository.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ww.helper.RandomHelper.randomElement;

@Service
public class BookService {

    public static int BOOK_SHELF_COUNT = 4;

    @Autowired
    BookRepository bookRepository;

    public Book findRandomBook() {
        return randomElement(bookRepository.findAll());
    }

}
