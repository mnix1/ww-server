package com.ww.model.dto.hero;

import com.ww.model.constant.HeroType;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.social.Profile;
import lombok.Getter;

@Getter
public class HeroDTO {

    private String namePolish;
    private String nameEnglish;
    private HeroType type;

    public HeroDTO(Hero hero) {
        this.namePolish = hero.getNamePolish();
        this.nameEnglish = hero.getNameEnglish();
        this.type = hero.getType();
    }
}
