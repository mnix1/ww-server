package com.ww.model.dto.hero;

import com.ww.model.constant.Category;
import com.ww.model.constant.hero.HeroType;
import com.ww.model.entity.hero.Hero;
import lombok.Getter;

import java.util.Set;

@Getter
public class HeroDTO {

    private Long id;
    private String namePolish;
    private String nameEnglish;
    private HeroType type;
    private Set<Category> hobbies;

    public HeroDTO(Hero hero) {
        this.id = hero.getId();
        this.namePolish = hero.getNamePolish();
        this.nameEnglish = hero.getNameEnglish();
        this.type = hero.getType();
        this.hobbies = hero.getHobbies();
    }
}
