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

    private Long gainCrystal;
    private Long gainElixir;
    private Long gainWisdom;

    private Boolean canBuyByGold;
    private Boolean canBuyByCrystal;

    private Long goldCost;
    private Long crystalCost;

    private String namePolish;
    private String nameEnglish;

    public ShopBookDTO(Book book) {
        this.id = book.getId();
        this.type = book.getType();
        this.readTime = book.getReadTime();
        this.level = book.getLevel();
        this.gainCrystal = book.getGainCrystal();
        this.gainElixir = book.getGainElixir();
        this.gainWisdom = book.getGainWisdom();
        this.canBuyByGold = book.getCanBuyByGold();
        this.canBuyByCrystal = book.getCanBuyByCrystal();
        this.goldCost = book.getGoldCost();
        this.crystalCost = book.getCrystalCost();
        this.namePolish = book.getNamePolish();
        this.nameEnglish = book.getNameEnglish();
    }
}
