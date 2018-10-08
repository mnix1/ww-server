package com.ww.model.dto.wisie;

import com.ww.model.constant.Category;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.container.rival.war.WarWisie;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
public class WarProfileWisieDTO {
    private String namePolish;
    private String nameEnglish;
    private WisieType type;
    private Double value;
    private Set<Category> hobbies;

    public WarProfileWisieDTO(WarWisie warWisie) {
        this.namePolish = warWisie.getWisie().getWisie().getNamePolish();
        this.nameEnglish = warWisie.getWisie().getWisie().getNameEnglish();
        this.type = warWisie.getWisie().getWisie().getType();
        this.value = warWisie.getWisie().calculateValue();
        this.hobbies = warWisie.getWisie().getHobbies();
    }
}
