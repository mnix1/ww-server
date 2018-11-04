package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.*;

public class PlayIntroState extends PlayState {
    public PlayIntroState(PlayManager manager) {
        super(manager, RivalStatus.INTRO);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelImportanceType(model, getContainer());
        fillModelProfiles(model, team, opponentTeam);
        fillModelSeasons(model, getContainer(), team, opponentTeam);
        return model;
    }

    @Override
    public long afterInterval() {
        return manager.getInterval().getIntroInterval();
    }

    @Override
    public void after() {
        String stateName = "CHOOSING_TASK_CATEGORY";
        if (getContainer().isEnd()) {
            stateName = "END";
        } else if (getContainer().isRandomTaskProps()) {
            stateName = "RANDOM_TASK_PROPS";
        }
        manager.getFlow().run(stateName);
    }
}
