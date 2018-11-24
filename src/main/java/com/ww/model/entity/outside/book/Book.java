package com.ww.model.entity.outside.book;


import com.ww.model.constant.book.BookType;
import com.ww.model.container.Resources;
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

    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;

    private Boolean canBuyByGold;
    private Boolean canBuyByCrystal;

    private Long goldCost;
    private Long crystalCost;

    public Book(BookType type, int readTimeHours, Integer level, Long crystalGain, Long wisdomGain, Long elixirGain, Boolean canBuyByGold, Boolean canBuyByCrystal, Long goldCost, Long crystalCost) {
        this.type = type;
        this.readTime = ((long) readTimeHours) * 1000 * 3600;
//        this.readTime = ((long) readTimeHours) * 1000 * 900;
        this.level = level;
        this.crystalGain = crystalGain;
        this.wisdomGain = wisdomGain;
        this.elixirGain = elixirGain;
        this.canBuyByGold = canBuyByGold;
        this.canBuyByCrystal = canBuyByCrystal;
        this.goldCost = goldCost;
        this.crystalCost = crystalCost;
    }

    public Resources getGainResources() {
        return new Resources(null, crystalGain, wisdomGain, elixirGain);
    }

    public Resources getCostResources() {
        if (canBuyByCrystal) {
            return new Resources(null, crystalCost, null, null);
        }
        if (canBuyByGold) {
            return new Resources(goldCost, null, null, null);
        }
        throw new IllegalArgumentException("no cost resources");
    }

    public Resources speedUpCost() {
        long crystalCost = (long) Math.ceil((getReadTime() - 1) / 1000d / 3600d);
        return new Resources(null, crystalCost, null, null);
    }

    @Override
    public boolean equals(Object obj) {
        return id.equals(((Book) obj).id);
    }
}
