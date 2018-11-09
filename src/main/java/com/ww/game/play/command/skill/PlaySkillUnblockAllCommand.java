package com.ww.game.play.command.skill;

import com.ww.game.GameCommand;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillUnblockAllCommand extends GameCommand {
    private WarTeam warTeam;

    public PlaySkillUnblockAllCommand(WarTeam warTeam) {
        this.warTeam = warTeam;
    }

    @Override
    public void execute() {
        warTeam.getTeamSkills().unblockAll();
    }
}
