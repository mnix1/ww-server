package com.ww.model.container.rival.war;

import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class WisorTeamMember extends TeamMember {
    private Profile content;
    private ProfileDTO contentDTO;
    private List<DisguiseType> disguises = new CopyOnWriteArrayList<>();

    public WisorTeamMember(int index, Profile content, ProfileDTO contentDTO) {
        super(index, HeroType.WISOR);
        this.content = content;
        this.contentDTO = contentDTO;
    }

}
