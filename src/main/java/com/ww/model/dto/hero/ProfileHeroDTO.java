package com.ww.model.dto.hero;

import com.ww.model.constant.Category;
import com.ww.model.constant.hero.HeroType;
import com.ww.model.entity.hero.ProfileHero;
import lombok.Getter;

import java.util.Set;

import static com.ww.helper.NumberHelper.smartRound;

@Getter
public class ProfileHeroDTO {

    private Long id;
    private HeroType type;

    private Boolean inTeam;

    private Set<Category> hobbies;

    private Double memory;
    private Double logic;
    private Double perceptivity;
    private Double counting;
    private Double combiningFacts;
    private Double patternRecognition;
    private Double imagination;

    private Double speed;
    private Double reflex;
    private Double concentration;
    private Double confidence;
    private Double intuition;

    private Double value;

    public ProfileHeroDTO(ProfileHero profileHero) {
        this.id = profileHero.getId();
        this.type = profileHero.getHero().getType();
        this.inTeam = profileHero.getInTeam();
        this.hobbies = profileHero.getHobbies();
        this.memory = smartRound(profileHero.getWisdomAttributeMemory());
        this.logic = smartRound(profileHero.getWisdomAttributeLogic());
        this.perceptivity = smartRound(profileHero.getWisdomAttributePerceptivity());
        this.counting = smartRound(profileHero.getWisdomAttributeCounting());
        this.combiningFacts = smartRound(profileHero.getWisdomAttributeCombiningFacts());
        this.patternRecognition = smartRound(profileHero.getWisdomAttributePatternRecognition());
        this.imagination = smartRound(profileHero.getWisdomAttributeImagination());
        this.speed = smartRound(profileHero.getMentalAttributeSpeed());
        this.reflex = smartRound(profileHero.getMentalAttributeReflex());
        this.concentration = smartRound(profileHero.getMentalAttributeConcentration());
        this.confidence = smartRound(profileHero.getMentalAttributeConfidence());
        this.intuition = smartRound(profileHero.getMentalAttributeIntuition());
        this.value = profileHero.calculateValue();
    }

}
