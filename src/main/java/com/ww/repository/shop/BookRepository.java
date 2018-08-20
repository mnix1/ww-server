package com.ww.repository.shop;

import com.ww.model.constant.shop.BookType;
import com.ww.model.entity.shop.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    Book findFirstByType(BookType bookType);
}
