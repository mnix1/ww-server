package com.ww.model.dto.wisie;

import com.ww.model.constant.Category;
import com.ww.model.constant.Skill;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
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
    private Set<Skill> skills;

    private Double value;

    public ProfileWisieDTO(ProfileWisie profileWisie) {
        this.id = profileWisie.getId();
        this.type = profileWisie.getType();
        this.inTeam = profileWisie.getInTeam();
        this.hobbies = profileWisie.getHobbies();
        this.skills = profileWisie.getSkills();
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            setWisdomAttributeValue(wisdomAttribute, smartRound(profileWisie.getWisdomAttributeValue(wisdomAttribute)));
        }
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            setMentalAttributeValue(mentalAttribute, smartRound(profileWisie.getMentalAttributeValue(mentalAttribute)));
        }
        this.value = profileWisie.calculateValue();
    }

}
