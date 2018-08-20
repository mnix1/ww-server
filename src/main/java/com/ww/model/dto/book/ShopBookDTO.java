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

    private Long gainDiamond;
    private Long gainElixir;
    private Long gainWisdom;

    private Boolean canBuyByGold;
    private Boolean canBuyByDiamond;

    private Long goldCost;
    private Long diamondCost;

    private String namePolish;
    private String nameEnglish;

    public ShopBookDTO(Book book) {
        this.type = book.getType();
        this.readTime = book.getReadTime();
        this.level = book.getLevel();
        this.gainDiamond = book.getGainDiamond();
        this.gainElixir = book.getGainElixir();
        this.gainWisdom = book.getGainWisdom();
        this.canBuyByGold = book.getCanBuyByGold();
        this.canBuyByDiamond = book.getCanBuyByDiamond();
        this.goldCost = book.getGoldCost();
        this.diamondCost = book.getDiamondCost();
        this.namePolish = book.getNamePolish();
        this.nameEnglish = book.getNameEnglish();
    }
}
