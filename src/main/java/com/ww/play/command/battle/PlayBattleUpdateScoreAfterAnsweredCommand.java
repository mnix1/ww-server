package com.ww.play.command.battle;

import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.play.command.PlayCommand;
import com.ww.play.container.PlayContainer;

public class PlayBattleUpdateScoreAfterAnsweredCommand extends PlayCommand {
    private Long profileId;
    private boolean isCorrect;

    public PlayBattleUpdateScoreAfterAnsweredCommand(PlayContainer container, Long profileId, boolean isCorrect) {
        super(container);
        this.profileId = profileId;
        this.isCorrect = isCorrect;
    }

    @Override
    public void execute() {
        BattleTeam battleTeam = (BattleTeam) container.getTeams().team(profileId);
        battleTeam.updateScore(isCorrect, container.getTasks().question().getDifficultyLevel().getPoints());
    }
}
