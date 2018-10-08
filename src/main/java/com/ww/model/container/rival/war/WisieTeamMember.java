package com.ww.model.container.rival.war;

import com.ww.model.constant.wisie.HeroType;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import lombok.Getter;

@Getter
public class WisieTeamMember extends TeamMember {
    private WarWisie content;
    private WarProfileWisieDTO contentDTO;

    public WisieTeamMember(int index, WarWisie content, WarProfileWisieDTO contentDTO) {
        super(index, HeroType.WISIE);
        this.content = content;
        this.contentDTO = contentDTO;
    }

    public void decreaseAttributesByHalf() {
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            content.setWisdomAttributeValue(wisdomAttribute, content.getWisdomAttributeValue(wisdomAttribute) / 2);
        }
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            content.setMentalAttributeValue(mentalAttribute, content.getMentalAttributeValue(mentalAttribute) / 2);
        }
        content.cacheAttributes();
        contentDTO = new WarProfileWisieDTO(content);
    }

}
