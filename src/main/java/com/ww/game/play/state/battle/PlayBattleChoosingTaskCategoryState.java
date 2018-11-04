package com.ww.game.play.state.battle;

import com.ww.game.play.PlayManager;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.PlayChoosingTaskCategoryState;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayBattleModelFiller.fillModelScores;

public class PlayBattleChoosingTaskCategoryState extends PlayChoosingTaskCategoryState {

    public PlayBattleChoosingTaskCategoryState(PlayManager manager) {
        super(manager);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelScores(model, (BattleTeam) team, (BattleTeam) opponentTeam);
        return model;
    }
}
