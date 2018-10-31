package com.ww.game.play.state.war;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.PlayPreparingNextTaskState;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveIndexes;

public class PlayWarPreparingNextTaskState extends PlayPreparingNextTaskState {
    public PlayWarPreparingNextTaskState(PlayContainer container, long interval) {
        super(container, interval);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelActiveIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }
}
