package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.state.PlayState;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;

public class PlayChangingTaskState extends PlayState {
    public PlayChangingTaskState(PlayManager manager) {
        super(manager, RivalStatus.CHANGING_TASK);
    }

    @Override
    public long afterInterval() {
        return manager.getInterval().getChangingTaskInterval();
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelPresentIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    public void after() {
        String stateName = "RANDOM_TASK_PROPS";
        if (getContainer().isEnd()) {
            stateName = "END";
        }
        manager.getFlow().run(stateName);
    }
}
