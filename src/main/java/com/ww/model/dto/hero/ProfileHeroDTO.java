package com.ww.model.dto.hero;

import com.ww.model.constant.hero.HeroType;
import com.ww.model.constant.hero.MentalAttribute;
import com.ww.model.constant.hero.WisdomAttribute;
import com.ww.model.entity.hero.ProfileHero;
import lombok.Getter;

import static com.ww.helper.NumberHelper.round2;

@Getter
public class ProfileHeroDTO {

    private Long id;
    private HeroType type;

    private Boolean inTeam;

    private Double memory;
    private Double logic;
    private Double perceptivity;
    private Double counting;
    private Double combiningFacts;
    private Double patternRecognition;
    private Double imagination;

    private Double reflex;
    private Double concentration;
    private Double leadership;
    private Double charisma;
    private Double intuition;

    private Double value;

    public ProfileHeroDTO(ProfileHero profileHero) {
        this.id = profileHero.getId();
        this.type = profileHero.getHero().getType();
        this.inTeam = profileHero.getInTeam();
        this.memory = round2(profileHero.getWisdomAttributeMemory());
        this.logic = round2(profileHero.getWisdomAttributeLogic());
        this.perceptivity = round2(profileHero.getWisdomAttributePerceptivity());
        this.counting = round2(profileHero.getWisdomAttributeCounting());
        this.combiningFacts = round2(profileHero.getWisdomAttributeCombiningFacts());
        this.patternRecognition = round2(profileHero.getWisdomAttributePatternRecognition());
        this.imagination = round2(profileHero.getWisdomAttributeImagination());
        this.reflex = round2(profileHero.getMentalAttributeReflex());
        this.concentration = round2(profileHero.getMentalAttributeConcentration());
        this.leadership = round2(profileHero.getMentalAttributeLeadership());
        this.charisma = round2(profileHero.getMentalAttributeCharisma());
        this.intuition = round2(profileHero.getMentalAttributeIntuition());
        prepareValue(profileHero);
    }

    private void prepareValue(ProfileHero profileHero) {
        this.value = 0d;
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            this.value += profileHero.getWisdomAttributeValue(wisdomAttribute);
        }
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            this.value += profileHero.getMentalAttributeValue(mentalAttribute);
        }
        this.value /= WisdomAttribute.COUNT + MentalAttribute.COUNT;
        this.value = round2(this.value);
    }

}
