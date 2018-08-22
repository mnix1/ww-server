package com.ww.model.dto.hero;

import com.ww.model.constant.hero.HeroType;
import com.ww.model.entity.hero.ProfileHero;
import lombok.Getter;

@Getter
public class ProfileHeroDTO {

    private Long id;
    private HeroType type;

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

    public ProfileHeroDTO(ProfileHero profileHero) {
        this.id = profileHero.getId();
        this.type = profileHero.getHero().getType();
        this.memory = profileHero.getWisdomAttributeMemory();
        this.logic = profileHero.getWisdomAttributeLogic();
        this.perceptivity = profileHero.getWisdomAttributePerceptivity();
        this.counting = profileHero.getWisdomAttributeCounting();
        this.combiningFacts = profileHero.getWisdomAttributeCombiningFacts();
        this.patternRecognition = profileHero.getWisdomAttributePatternRecognition();
        this.imagination = profileHero.getWisdomAttributeImagination();
        this.reflex = profileHero.getMentalAttributeReflex();
        this.concentration = profileHero.getMentalAttributeConcentration();
        this.leadership = profileHero.getMentalAttributeLeadership();
        this.charisma = profileHero.getMentalAttributeCharisma();
        this.intuition = profileHero.getMentalAttributeIntuition();
    }

}
