package com.ww.repository.book;

import com.ww.model.entity.book.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findAllByCanBuyByCrystalOrCanBuyByGold(Boolean canBuyByCrystal, Boolean canBuyByGold);
    List<Book> findAll();
}
