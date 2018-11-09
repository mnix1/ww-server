package com.ww.game.play.command.skill;

import com.ww.game.GameCommand;
import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillBlockAllCommand extends GameCommand {
    private WarTeam warTeam;

    public PlaySkillBlockAllCommand(WarTeam warTeam) {
        this.warTeam = warTeam;
    }

    @Override
    public void execute() {
        warTeam.getTeamSkills().blockAll();
    }
}
