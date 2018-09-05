package com.ww.model.dto.book;

import com.ww.model.constant.book.BookType;
import com.ww.model.entity.book.Book;
import lombok.Getter;

@Getter
public class ShopBookDTO {

    private Long id;
    private BookType type;

    private Long readTime;
    private Integer level;

    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;

    private Boolean canBuyByGold;
    private Boolean canBuyByCrystal;

    private Long goldCost;
    private Long crystalCost;

    public ShopBookDTO(Book book) {
        this.id = book.getId();
        this.type = book.getType();
        this.readTime = book.getReadTime();
        this.level = book.getLevel();
        this.crystalGain = book.getCrystalGain();
        this.wisdomGain = book.getWisdomGain();
        this.elixirGain = book.getElixirGain();
        this.canBuyByGold = book.getCanBuyByGold();
        this.canBuyByCrystal = book.getCanBuyByCrystal();
        this.goldCost = book.getGoldCost();
        this.crystalCost = book.getCrystalCost();
    }
}
