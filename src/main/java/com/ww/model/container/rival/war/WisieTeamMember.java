package com.ww.model.container.rival.war;

import com.ww.model.constant.wisie.HeroType;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.outside.wisie.OwnedWisie;
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

        contentDTO = new WarProfileWisieDTO(content);
    }

}
