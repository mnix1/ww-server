package com.ww.service.book;

import com.ww.model.constant.book.BookType;
import com.ww.model.entity.outside.book.Book;
import com.ww.repository.outside.book.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;

@Service
@AllArgsConstructor
public class BookService {

    public static int BOOK_SHELF_COUNT = 10;

    private final BookRepository bookRepository;

    public Book findBook(int minLevel) {
        return randomElement(bookRepository.findAll().stream().filter(book -> book.getLevel() >= minLevel).collect(Collectors.toList()));
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findBookByType(BookType bookType) {
        return bookRepository.findOneByType(bookType).get();
    }

}
