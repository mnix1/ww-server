package com.ww.model.container.rival.war;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.dto.social.RivalProfileDTO;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
