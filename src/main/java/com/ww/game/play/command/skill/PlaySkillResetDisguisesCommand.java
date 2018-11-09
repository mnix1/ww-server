package com.ww.game.play.command.skill;

import com.ww.game.GameCommand;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillResetDisguisesCommand extends GameCommand {
    private WarTeam warTeam;

    public PlaySkillResetDisguisesCommand(WarTeam warTeam) {
        this.warTeam = warTeam;
    }

    @Override
    public void execute() {
        warTeam.resetDisguises();
    }
}
