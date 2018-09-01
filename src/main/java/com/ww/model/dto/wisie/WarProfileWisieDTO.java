package com.ww.model.dto.wisie;

import com.ww.model.constant.Category;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.entity.wisie.ProfileWisie;
import lombok.Getter;

import java.util.Set;

@Getter
public class WarProfileWisieDTO {
    private String namePolish;
    private String nameEnglish;
    private WisieType type;
    private Double value;
    private Set<Category> hobbies;

    public WarProfileWisieDTO(ProfileWisie profileWisie) {
        this.namePolish = profileWisie.getWisie().getNamePolish();
        this.nameEnglish = profileWisie.getWisie().getNameEnglish();
        this.type = profileWisie.getWisie().getType();
        this.value = profileWisie.calculateValue();
        this.hobbies = profileWisie.getHobbies();
    }


}
