package com.ww.model.dto.wisie;

import com.ww.model.constant.wisie.WisieType;
import com.ww.model.entity.outside.wisie.Wisie;
import lombok.Getter;

@Getter
public class WisieDTO {

    private String namePolish;
    private String nameEnglish;
    private WisieType type;

    public WisieDTO(Wisie wisie) {
        this.namePolish = wisie.getNamePolish();
        this.nameEnglish = wisie.getNameEnglish();
        this.type = wisie.getType();
    }
}
