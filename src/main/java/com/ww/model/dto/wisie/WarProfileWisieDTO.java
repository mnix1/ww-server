package com.ww.model.dto.wisie;

import com.ww.model.constant.Category;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.entity.wisie.OwnedWisie;
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

    public WarProfileWisieDTO(OwnedWisie ownedWisie) {
        this.namePolish = ownedWisie.getWisie().getNamePolish();
        this.nameEnglish = ownedWisie.getWisie().getNameEnglish();
        this.type = ownedWisie.getWisie().getType();
        this.value = ownedWisie.calculateValue();
        this.hobbies = ownedWisie.getHobbies();
    }


}
