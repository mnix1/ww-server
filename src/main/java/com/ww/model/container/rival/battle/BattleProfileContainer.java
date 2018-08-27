package com.ww.model.container.rival.battle;

import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleProfileContainer extends RivalProfileContainer {
    public BattleProfileContainer(Profile profile, Long opponentId) {
        super(profile, opponentId);
    }
}
