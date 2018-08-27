package com.ww.model.dto.hero;

import com.ww.model.constant.hero.HeroType;
import com.ww.model.entity.hero.ProfileHero;
import lombok.Getter;

@Getter
public class WarProfileHeroDTO {
    private String namePolish;
    private String nameEnglish;
    private HeroType type;
    private Double value;

    public WarProfileHeroDTO(ProfileHero profileHero) {
        this.namePolish = profileHero.getHero().getNamePolish();
        this.nameEnglish = profileHero.getHero().getNameEnglish();
        this.type = profileHero.getHero().getType();
        this.value = profileHero.calculateValue();
    }


}
