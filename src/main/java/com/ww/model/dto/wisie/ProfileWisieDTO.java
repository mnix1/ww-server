package com.ww.model.dto.wisie;

import com.ww.model.constant.Category;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.entity.outside.wisie.AbstractWisieAttributes;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import lombok.Getter;

import java.util.Set;

import static com.ww.helper.NumberHelper.smartRound;

@Getter
public class ProfileWisieDTO extends AbstractWisieAttributes {

    private Long id;
    private WisieType type;

    private Boolean inTeam;

    private Set<Category> hobbies;

    private Double value;

    public ProfileWisieDTO(ProfileWisie profileWisie) {
        this.id = profileWisie.getId();
        this.type = profileWisie.getWisie().getType();
        this.inTeam = profileWisie.getInTeam();
        this.hobbies = profileWisie.getHobbies();
        this.memory = smartRound(profileWisie.getMemory());
        this.logic = smartRound(profileWisie.getLogic());
        this.perceptivity = smartRound(profileWisie.getPerceptivity());
        this.counting = smartRound(profileWisie.getCounting());
        this.combiningFacts = smartRound(profileWisie.getCombiningFacts());
        this.patternRecognition = smartRound(profileWisie.getPatternRecognition());
        this.imagination = smartRound(profileWisie.getImagination());
        this.speed = smartRound(profileWisie.getSpeed());
        this.reflex = smartRound(profileWisie.getReflex());
        this.concentration = smartRound(profileWisie.getConcentration());
        this.confidence = smartRound(profileWisie.getConfidence());
        this.intuition = smartRound(profileWisie.getIntuition());
        this.value = profileWisie.calculateValue();
    }

}
