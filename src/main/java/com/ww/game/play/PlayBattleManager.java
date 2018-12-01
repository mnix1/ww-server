package com.ww.game.play;

import com.ww.game.play.communication.PlayCommunication;
import com.ww.game.play.container.PlayBattleContainer;
import com.ww.game.play.flow.PlayBattleFlow;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.RivalService;
import com.ww.service.rival.battle.RivalBattleService;

public class PlayBattleManager extends PlayManager {
    public static final Integer TASK_COUNT = 5;

    public PlayBattleManager(RivalTwoInit init, RivalBattleService rivalService) {
        super(rivalService);
        this.interval = prepareInterval();
        this.container = new PlayBattleContainer(init, prepareTeams(init), prepareTasks(), prepareTimeouts(), prepareDecisions(), prepareResult());
        this.flow = new PlayBattleFlow(this);
        this.communication = new PlayCommunication(this);
    }

    protected RivalInterval prepareInterval() {
        return new RivalInterval();
    }

    protected RivalTeams prepareTeams(RivalTwoInit init) {
        Profile creatorProfile = init.getCreatorProfile();
        Profile opponentProfile = init.getOpponentProfile();
        return new RivalTeams(new BattleTeam(creatorProfile), new BattleTeam(opponentProfile));
    }
}
