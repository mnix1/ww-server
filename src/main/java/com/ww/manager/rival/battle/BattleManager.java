package com.ww.manager.rival.battle;

import com.ww.manager.rival.RivalManager;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.battle.*;
import com.ww.model.container.rival.init.RivalTwoPlayerInit;
import com.ww.service.rival.battle.RivalBattleService;
import com.ww.service.social.ProfileConnectionService;
import lombok.Getter;

@Getter
public class BattleManager extends RivalManager {

    public static final Integer TASK_COUNT = 5;

    public BattleModel model;
    public BattleModelFactory modelFactory;
    public RivalInterval interval;
    public BattleFlow flow;

    public BattleManager(RivalTwoPlayerInit init, RivalBattleService rivalService, ProfileConnectionService profileConnectionService) {
        this.abstractRivalService = rivalService;
        this.profileConnectionService = profileConnectionService;
        this.model = new BattleModel(init, new BattleTeams());
        this.model.getTeamsContainer().addProfile(init.getCreatorProfile().getId(), new BattleTeam(init.getCreatorProfile()));
        this.model.getTeamsContainer().addProfile(init.getOpponentProfile().getId(), new BattleTeam(init.getOpponentProfile()));
        this.modelFactory = new BattleModelFactory(this.model);
        this.interval = new RivalInterval();
        this.flow = new BattleFlow(this);
    }

    public boolean isEnd() {
        return model.getCurrentTaskIndex() == TASK_COUNT - 1;
    }
}
