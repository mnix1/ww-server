package com.ww.play.state.battle;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.play.command.battle.PlayBattleUpdateScoreAfterAnsweredCommand;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.PlayAnsweredState;

import java.util.Map;

import static com.ww.play.modelfiller.PlayBattleModelFiller.fillModelNewScores;

public class PlayBattleAnsweredState extends PlayAnsweredState {

    public PlayBattleAnsweredState(PlayContainer container, Long profileId, Long answerId) {
        super(container, profileId, answerId);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        boolean isCorrect = container.getTasks().correctAnswerId().equals(answerId);
        commands.add(new PlayBattleUpdateScoreAfterAnsweredCommand(container, profileId, isCorrect));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNewScores(model, (BattleTeam) team, (BattleTeam) opponentTeam);
        return model;
    }
}
