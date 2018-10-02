package com.ww.manager.rival.battle;

import com.ww.manager.rival.RivalManager;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.battle.*;
import com.ww.model.container.rival.init.RivalTwoPlayerInitContainer;
import com.ww.service.rival.battle.RivalBattleService;
import com.ww.service.social.ProfileConnectionService;
import lombok.Getter;

@Getter
public class BattleManager extends RivalManager {

    public static final Integer TASK_COUNT = 5;

    public BattleContainer container;
    public BattleModelFactory modelFactory;
    public RivalInterval interval;
    public BattleFlow flow;

    public BattleManager(RivalTwoPlayerInitContainer container, RivalBattleService rivalBattleService, ProfileConnectionService profileConnectionService) {
        this.abstractRivalService = rivalBattleService;
        this.profileConnectionService = profileConnectionService;
        this.container = new BattleContainer(container, new BattleTeamsContainer());
        this.container.getTeamsContainer().addProfile(container.getCreatorProfile().getId(), new BattleProfileContainer(container.getCreatorProfile()));
        this.container.getTeamsContainer().addProfile(container.getOpponentProfile().getId(), new BattleProfileContainer(container.getOpponentProfile()));
        this.modelFactory = new BattleModelFactory(this.container);
        this.interval = new RivalInterval();
        this.flow = new BattleFlow(this);
    }

    public boolean isEnd() {
        return container.getCurrentTaskIndex() == TASK_COUNT - 1;
    }
}
