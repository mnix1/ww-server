package com.ww.model.dto.task;

import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.rival.battle.Battle;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class BattleInfoDTO {

    private Long id;
    private ProfileDTO creatorProfile;
    private Date inProgressDate;

    public BattleInfoDTO(Battle battle) {
        this.id = battle.getId();
        this.creatorProfile = new ProfileDTO(battle.getCreatorProfile());
        this.inProgressDate = battle.getInProgressDate();
    }
}
