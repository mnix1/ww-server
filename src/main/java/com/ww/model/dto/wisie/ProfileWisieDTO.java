package com.ww.model.dto.wisie;

import com.ww.model.constant.Category;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.entity.wisie.ProfileWisie;
import lombok.Getter;

import java.util.Set;

import static com.ww.helper.NumberHelper.smartRound;

@Getter
public class ProfileWisieDTO {

    private Long id;
    private WisieType type;

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

    public ProfileWisieDTO(ProfileWisie profileWisie) {
        this.id = profileWisie.getId();
        this.type = profileWisie.getWisie().getType();
        this.inTeam = profileWisie.getInTeam();
        this.hobbies = profileWisie.getHobbies();
        this.memory = smartRound(profileWisie.getWisdomAttributeMemory());
        this.logic = smartRound(profileWisie.getWisdomAttributeLogic());
        this.perceptivity = smartRound(profileWisie.getWisdomAttributePerceptivity());
        this.counting = smartRound(profileWisie.getWisdomAttributeCounting());
        this.combiningFacts = smartRound(profileWisie.getWisdomAttributeCombiningFacts());
        this.patternRecognition = smartRound(profileWisie.getWisdomAttributePatternRecognition());
        this.imagination = smartRound(profileWisie.getWisdomAttributeImagination());
        this.speed = smartRound(profileWisie.getMentalAttributeSpeed());
        this.reflex = smartRound(profileWisie.getMentalAttributeReflex());
        this.concentration = smartRound(profileWisie.getMentalAttributeConcentration());
        this.confidence = smartRound(profileWisie.getMentalAttributeConfidence());
        this.intuition = smartRound(profileWisie.getMentalAttributeIntuition());
        this.value = profileWisie.calculateValue();
    }

}
