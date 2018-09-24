package com.ww.model.container.rival.battle;

import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleProfileContainer extends RivalProfileContainer {
    private Integer score = 0;
    public BattleProfileContainer(Profile profile, Long opponentId) {
        super(profile, opponentId);
    }
}
