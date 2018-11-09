package com.ww.game.play.command.skill;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.flow.PlaySkillFlowCommand;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillEnableTeamMemberCommand extends PlaySkillFlowCommand {
    private WarTeam warTeam;
    private Integer index;

    public PlaySkillEnableTeamMemberCommand(PlayManager manager, WarTeam warTeam, Integer index) {
        super(manager);
        this.warTeam = warTeam;
        this.index = index;
    }

    @Override
    public void execute() {
        warTeam.getTeamMembers().get(index).setPresent(true);
    }
}
