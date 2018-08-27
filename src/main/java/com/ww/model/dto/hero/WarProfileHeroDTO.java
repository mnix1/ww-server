package com.ww.model.dto.hero;

import com.ww.model.constant.hero.HeroType;
import com.ww.model.constant.hero.MentalAttribute;
import com.ww.model.constant.hero.WisdomAttribute;
import com.ww.model.entity.hero.ProfileHero;
import lombok.Getter;

import static com.ww.helper.NumberHelper.round2;

@Getter
public class WarProfileHeroDTO {
    private HeroType type;
    private Double value;

    public WarProfileHeroDTO(ProfileHero profileHero) {
        this.type = profileHero.getHero().getType();
        this.value = profileHero.calculateValue();
    }


}
