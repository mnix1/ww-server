package com.ww.model.dto.hero;

import com.ww.model.constant.Category;
import com.ww.model.constant.hero.HeroType;
import com.ww.model.entity.hero.ProfileHero;
import lombok.Getter;

import java.util.Set;

@Getter
public class WarProfileHeroDTO {
    private String namePolish;
    private String nameEnglish;
    private HeroType type;
    private Double value;
    private Set<Category> hobbies;

    public WarProfileHeroDTO(ProfileHero profileHero) {
        this.namePolish = profileHero.getHero().getNamePolish();
        this.nameEnglish = profileHero.getHero().getNameEnglish();
        this.type = profileHero.getHero().getType();
        this.value = profileHero.calculateValue();
        this.hobbies = profileHero.getHero().getHobbies();
    }


}
