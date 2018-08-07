package com.ww.model.dto.hero;

import com.ww.model.constant.Category;
import com.ww.model.constant.hero.HeroType;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.hero.ProfileHero;
import lombok.Getter;

import java.util.Set;

@Getter
public class HeroDTO {

    private String namePolish;
    private String nameEnglish;
    private HeroType type;
    private Set<Category> hobbies;

    private Boolean isOwned;

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

    public HeroDTO(Hero hero, ProfileHero profileHero) {
        this.namePolish = hero.getNamePolish();
        this.nameEnglish = hero.getNameEnglish();
        this.type = hero.getType();
        this.hobbies = hero.getHobbies();
        if (profileHero == null) {
            this.isOwned = false;
            return;
        }
        this.isOwned = true;
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
