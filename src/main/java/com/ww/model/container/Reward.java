package com.ww.model.container;

import com.ww.model.constant.book.BookType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Reward {

    private Resources resources;
    private BookType bookType;

    public void writeToMap(Map<String,Object> model) {
        if (resources.getGold() != null) {
            model.put("goldGain", resources.getGold());
        }
        if (resources.getCrystal() != null) {
            model.put("crystalGain", resources.getCrystal());
        }
        if (resources.getWisdom() != null) {
            model.put("wisdomGain", resources.getWisdom());
        }
        if (resources.getElixir() != null) {
            model.put("elixirGain", resources.getElixir());
        }
        if (bookType != null) {
            model.put("bookType", bookType.name());
        }
    }
}
