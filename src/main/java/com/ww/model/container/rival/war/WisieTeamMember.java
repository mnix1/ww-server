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

    private void refreshCache() {
        content.cacheAttributes();
        contentDTO = new WarProfileWisieDTO(content);
    }

    public void decreaseAttributesByHalf() {
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            content.setWisdomAttributeValue(wisdomAttribute, content.getWisdomAttributeValue(wisdomAttribute) / 2);
        }
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            content.setMentalAttributeValue(mentalAttribute, content.getMentalAttributeValue(mentalAttribute) / 2);
        }
        refreshCache();
    }

    public void increaseWisdomAttributes(WarWisie source, double factor) {
        for (WisdomAttribute wisdomAttribute : WisdomAttribute.values()) {
            double actualValue = content.getWisdomAttributeValue(wisdomAttribute);
            double changeValue = source.getWisie().getWisdomAttributeValue(wisdomAttribute) * factor;
            content.setWisdomAttributeValue(wisdomAttribute, actualValue + changeValue);
        }
//        refreshCache();
    }

    public void increaseMentalAttributes(WarWisie source, double factor) {
        for (MentalAttribute mentalAttribute : MentalAttribute.values()) {
            double actualValue = content.getMentalAttributeValue(mentalAttribute);
            double changeValue = source.getWisie().getMentalAttributeValue(mentalAttribute) * factor;
            content.setMentalAttributeValue(mentalAttribute, actualValue + changeValue);
        }
//        refreshCache();
    }

}
