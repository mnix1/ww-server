package com.ww.model.dto.social;

import com.ww.model.constant.Grade;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

@Getter
public class RivalProfileDTO extends ProfileDTO {

    private Long battleElo;
    private Grade battleGrade;
    private Long warElo;
    private Grade warGrade;

    public RivalProfileDTO(Profile profile, RivalType rivalType) {
        super(profile);
        if (rivalType == RivalType.BATTLE) {
            this.battleElo = profile.getBattleElo();
            this.battleGrade = Grade.fromElo(battleElo);
        } else if (rivalType == RivalType.WAR) {
            this.warElo = profile.getWarElo();
            this.warGrade = Grade.fromElo(warElo);
        }
    }
}
