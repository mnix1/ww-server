package com.ww.game.play.command.skill;

import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillUseCommand extends PlayCommand {
    private WarTeam warTeam;
    private Skill skill;

    public PlaySkillUseCommand(PlayContainer container, WarTeam warTeam, Skill skill) {
        super(container);
        this.warTeam = warTeam;
        this.skill = skill;
    }

    @Override
    public void execute() {
        warTeam.getTeamSkills().use(skill);
    }
}
