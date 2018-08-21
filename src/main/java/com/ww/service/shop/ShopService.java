package com.ww.service.shop;

import com.ww.model.dto.book.ShopBookDTO;
import com.ww.repository.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {

    @Autowired
    BookRepository bookRepository;

    public List<ShopBookDTO> listBook() {
        return bookRepository.findAllByCanBuyByCrystalOrCanBuyByGold(true, true).stream().map(ShopBookDTO::new).collect(Collectors.toList());
    }

}
