package com.ww.play;

import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.entity.outside.social.Profile;
import com.ww.play.communication.PlayCommunication;
import com.ww.play.container.PlayBattleContainer;
import com.ww.play.flow.PlayBattleFlow;
import com.ww.play.flow.PlayFlow;
import com.ww.service.rival.battle.RivalBattleService;
import com.ww.websocket.message.Message;

public class PlayBattleManager extends PlayManager {
    public static final Integer TASK_COUNT = 5;

    public PlayBattleManager(RivalTwoInit init, RivalBattleService rivalService) {
        super(rivalService);
        this.container = new PlayBattleContainer(init, prepareTeams(init), prepareTasks(), prepareTimeouts(), prepareDecisions(), prepareResult());
        this.flow = new PlayBattleFlow(this, new RivalInterval());
        this.communication = new PlayCommunication(this, Message.BATTLE_CONTENT);
    }

    protected RivalTeams prepareTeams(RivalTwoInit init) {
        Profile creatorProfile = init.getCreatorProfile();
        Profile opponentProfile = init.getOpponentProfile();
        return new RivalTeams(new BattleTeam(creatorProfile), new BattleTeam(opponentProfile));
    }
}
