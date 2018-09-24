package com.ww.repository.outside.book;

import com.ww.model.constant.book.BookType;
import com.ww.model.entity.outside.book.Book;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    @Cacheable("canBuyBooks")
    List<Book> findAllByCanBuyByCrystalOrCanBuyByGold(Boolean canBuyByCrystal, Boolean canBuyByGold);
    @Cacheable("allBooks")
    List<Book> findAll();
    Optional<Book> findOneById(Long id);
    Optional<Book> findOneByType(BookType type);
}
