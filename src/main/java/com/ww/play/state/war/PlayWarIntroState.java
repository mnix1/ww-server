package com.ww.play.state.war;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.PlayIntroState;

import java.util.Map;

import static com.ww.play.modelfiller.PlayWarModelFiller.*;

public class PlayWarIntroState extends PlayIntroState {
    public PlayWarIntroState(PlayContainer container) {
        super(container);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        WarTeam warTeam = (WarTeam) team;
        WarTeam warOpponentTeam = (WarTeam) opponentTeam;
        fillModelPresentIndexes(model, warTeam, warOpponentTeam);
        fillModelSkills(model, warTeam, warOpponentTeam);
        fillModelTeams(model, warTeam, warOpponentTeam);
        return model;
    }
}
