package com.ww.play.state.battle;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.PlayChoosingTaskCategoryState;

import java.util.Map;

import static com.ww.play.modelfiller.PlayBattleModelFiller.fillModelScores;

public class PlayBattleChoosingTaskCategoryState extends PlayChoosingTaskCategoryState {

    public PlayBattleChoosingTaskCategoryState(PlayContainer container, long interval) {
        super(container, interval);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelScores(model, (BattleTeam) team, (BattleTeam) opponentTeam);
        return model;
    }
}
