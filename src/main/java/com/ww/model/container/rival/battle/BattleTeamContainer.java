package com.ww.model.container.rival.battle;

import com.ww.model.container.rival.RivalTeamContainer;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleTeamContainer extends RivalTeamContainer {
    private Integer score = 0;
    public BattleTeamContainer(Profile profile) {
        super(profile);
    }
}
