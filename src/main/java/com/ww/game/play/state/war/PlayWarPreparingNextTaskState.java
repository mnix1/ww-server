package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.state.PlayPreparingNextTaskState;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveIndexes;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;

public class PlayWarPreparingNextTaskState extends PlayPreparingNextTaskState {
    public PlayWarPreparingNextTaskState(PlayManager manager) {
        super(manager);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelActiveIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelActiveMemberAddOns(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }
}
