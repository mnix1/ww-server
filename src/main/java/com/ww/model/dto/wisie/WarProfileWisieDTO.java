package com.ww.model.dto.wisie;

import com.ww.model.constant.Category;
import com.ww.model.constant.Skill;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.constant.wisie.WisieValueChange;
import com.ww.model.container.rival.war.WarWisie;
import lombok.Getter;

import java.util.Set;

@Getter
public class WarProfileWisieDTO {
    private WisieType type;
    private WisieValueChange valueChange;
    private Double value;
    private Set<Category> hobbies;
    private Set<Skill> skills;

    public WarProfileWisieDTO(WarWisie warWisie) {
        this.type = warWisie.getWisie().getType();
        this.valueChange = warWisie.getWisieValueChange();
        this.value = warWisie.getValue();
        this.hobbies = warWisie.getWisie().getHobbies();
        this.skills = warWisie.getWisie().getSkills();
    }
}
