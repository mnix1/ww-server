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

    private Long gainCrystal;
    private Long gainElixir;
    private Long gainWisdom;

    private Boolean canBuyByGold;
    private Boolean canBuyByCrystal;

    private Long goldCost;
    private Long crystalCost;

    private String namePolish;
    private String nameEnglish;

    public Book(BookType type, int readTimeHours, Integer level, Long gainCrystal, Long gainElixir, Long gainWisdom, Boolean canBuyByGold, Boolean canBuyByCrystal, Long goldCost, Long crystalCost, String namePolish, String nameEnglish) {
        this.type = type;
        this.readTime = ((long) readTimeHours) * 1000 * 3600;
//        this.readTime = ((long) readTimeHours) * 1000 * 900;
        this.level = level;
        this.gainCrystal = gainCrystal;
        this.gainElixir = gainElixir;
        this.gainWisdom = gainWisdom;
        this.canBuyByGold = canBuyByGold;
        this.canBuyByCrystal = canBuyByCrystal;
        this.goldCost = goldCost;
        this.crystalCost = crystalCost;
        this.namePolish = namePolish;
        this.nameEnglish = nameEnglish;
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Book) obj).id);
    }
}
