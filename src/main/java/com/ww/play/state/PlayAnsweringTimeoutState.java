package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.play.container.PlayContainer;

import java.util.Map;

import static com.ww.play.modelfiller.PlayModelFiller.fillModelAnswered;
import static com.ww.play.modelfiller.PlayModelFiller.fillModelCorrectAnswerId;

public class PlayAnsweringTimeoutState extends PlayState {
    public PlayAnsweringTimeoutState(PlayContainer container) {
        super(container, RivalStatus.ANSWERING_TIMEOUT);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelCorrectAnswerId(model, container);
        return model;
    }
}
