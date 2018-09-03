package com.ww.model.dto.social;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.social.Profile;
import lombok.Getter;

@Getter
public class RivalProfileDTO extends ProfileDTO {

    private Long battleElo;
    private Long warElo;

    public RivalProfileDTO(Profile profile, RivalType rivalType) {
        super(profile);
        if (rivalType == RivalType.BATTLE) {
            this.battleElo = profile.getBattleElo();
        } else if (rivalType == RivalType.WAR) {
            this.warElo = profile.getWarElo();
        }
    }
}
