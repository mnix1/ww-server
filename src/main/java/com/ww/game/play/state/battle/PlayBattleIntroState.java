package com.ww.game.play.state.battle;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.PlayIntroState;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayBattleModelFiller.fillModelScores;
import static com.ww.game.play.modelfiller.PlayBattleModelFiller.fillModelTaskCount;

public class PlayBattleIntroState extends PlayIntroState {
    public PlayBattleIntroState(PlayContainer container) {
        super(container);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        BattleTeam battleTeam = (BattleTeam) team;
        BattleTeam battleOpponentTeam = (BattleTeam) opponentTeam;
        fillModelScores(model, battleTeam, battleOpponentTeam);
        fillModelTaskCount(model);
        return model;
    }
}
