package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.play.container.PlayContainer;

import java.util.Map;

public class PlayRandomTaskPropsState extends PlayState {
    public PlayRandomTaskPropsState(PlayContainer container) {
        super(container, RivalStatus.RANDOM_TASK_PROPS);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);

        return model;
    }
}
