package com.ww.model.container;

import com.ww.model.constant.book.BookType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RewardObject {

    private Long goldGain;
    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;
    private BookType bookType;

    public void writeToMap(Map<String,Object> model) {
        if (goldGain != null) {
            model.put("goldGain", goldGain);
        }
        if (crystalGain != null) {
            model.put("crystalGain", crystalGain);
        }
        if (wisdomGain != null) {
            model.put("wisdomGain", wisdomGain);
        }
        if (elixirGain != null) {
            model.put("elixirGain", elixirGain);
        }
        if (bookType != null) {
            model.put("bookType", bookType.name());
        }
    }
}
