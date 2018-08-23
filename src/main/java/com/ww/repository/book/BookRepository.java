package com.ww.repository.book;

import com.ww.model.entity.book.Book;
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
}
