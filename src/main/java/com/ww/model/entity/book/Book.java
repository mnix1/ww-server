package com.ww.model.entity.book;


import com.ww.model.constant.book.BookType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Book(BookType type, int readTimeHours, Integer level, Long gainDiamond, Long gainElixir, Long gainWisdom, Boolean canBuyByGold, Boolean canBuyByDiamond, Long goldCost, Long diamondCost, String namePolish, String nameEnglish) {
        this.type = type;
        this.readTime = ((long) readTimeHours) * 1000 * 3600;
        this.level = level;
        this.gainDiamond = gainDiamond;
        this.gainElixir = gainElixir;
        this.gainWisdom = gainWisdom;
        this.canBuyByGold = canBuyByGold;
        this.canBuyByDiamond = canBuyByDiamond;
        this.goldCost = goldCost;
        this.diamondCost = diamondCost;
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Book) obj).id);
    }
}
