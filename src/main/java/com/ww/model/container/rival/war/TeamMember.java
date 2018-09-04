package com.ww.model.container.rival.war;

import com.ww.model.constant.wisie.HeroType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMember {
    private int index;
    private HeroType type;
    private boolean present;
    private Object content;
    private Object contentDTO;

    public TeamMember(int index, HeroType type, Object content, Object contentDTO) {
        this.index = index;
        this.type = type;
        this.present = true;
        this.content = content;
        this.contentDTO = contentDTO;
    }

    public boolean isWisie() {
        return HeroType.isWisie(type);
    }
}
