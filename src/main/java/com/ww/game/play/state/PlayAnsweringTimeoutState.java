package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.game.play.container.PlayContainer;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelAnswered;
import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelCorrectAnswerId;

public class PlayAnsweringTimeoutState extends PlayState {
    public PlayAnsweringTimeoutState(PlayManager manager) {
        super(manager, RivalStatus.ANSWERING_TIMEOUT);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelCorrectAnswerId(model, getContainer());
        return model;
    }

    @Override
    public long afterInterval(){
        return manager.getInterval().getAnsweringTimeoutInterval();
    }

    @Override
    public void after(){
        String stateName = "CHOOSING_TASK_CATEGORY";
        if (getContainer().isEnd()) {
            stateName = "END";
        } else if (getContainer().isRandomTaskProps()) {
            stateName = "RANDOM_TASK_PROPS";
        }
        manager.getFlow().run(stateName);
    }
}
