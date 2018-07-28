package com.ww.model.dto.task;

import com.ww.model.constant.rival.battle.BattleProfileStatus;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.rival.battle.Battle;
import com.ww.model.entity.rival.battle.BattleProfile;
import com.ww.model.entity.social.Profile;
import lombok.Getter;

import java.util.Date;

@Getter
public class BattlePositionDTO {

    private Long position;
    private ProfileDTO profile;
    private Integer score = 0;
    private Long answerInterval;
    private BattleProfileStatus status;

    public BattlePositionDTO(BattleProfile battleProfile) {
        this.profile = new ProfileDTO(battleProfile.getProfile());
        this.status = battleProfile.getStatus();
        if (this.status == BattleProfileStatus.CLOSED) {
            this.answerInterval = battleProfile.inProgressInterval();
        }
    }

    public void increaseScore() {
        score++;
    }

}
