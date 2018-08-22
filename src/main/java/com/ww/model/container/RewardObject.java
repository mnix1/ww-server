package com.ww.model.container;

import com.ww.model.constant.book.BookType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RewardObject {

    private Long gainGold;
    private Long gainCrystal;
    private Long gainElixir;
    private Long gainWisdom;
    private BookType bookType;

    public void writeToMap(Map<String,Object> model) {
        if (gainGold != null) {
            model.put("gainGold", gainGold);
        }
        if (gainCrystal != null) {
            model.put("gainCrystal", gainCrystal);
        }
        if (gainElixir != null) {
            model.put("gainElixir", gainElixir);
        }
        if (gainWisdom != null) {
            model.put("gainWisdom", gainWisdom);
        }
        if (bookType != null) {
            model.put("bookType", bookType.name());
        }
    }
}
