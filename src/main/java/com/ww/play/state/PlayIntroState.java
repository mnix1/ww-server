package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.play.container.PlayContainer;

import java.util.Map;

import static com.ww.play.modelfiller.PlayModelFiller.*;

public class PlayIntroState extends PlayState {
    public PlayIntroState(PlayContainer container) {
        super(container, RivalStatus.INTRO);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelImportanceType(model, container);
        fillModelProfiles(model, team, opponentTeam);
        fillModelSeasons(model, container, team, opponentTeam);
        return model;
    }
}
