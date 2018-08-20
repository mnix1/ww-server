package com.ww.model.dto.shop;

import com.ww.model.constant.shop.BookType;
import com.ww.model.entity.shop.Book;
import com.ww.model.entity.shop.ProfileChest;
import lombok.Getter;

@Getter
public class BookDTO {

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

    public BookDTO(Book book) {
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

    public BookDTO(ProfileChest profileChest) {
        this(profileChest.getBook());
        this.id = profileChest.getId();
    }
}
