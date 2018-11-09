package com.ww.game.play.state.skill.lifebuoy;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.PlaySkillEnableTeamMemberCommand;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillLifebuoyActionState extends GameState {
    protected PlayManager manager;
    protected WarTeam warTeam;
    protected Integer index;

    public PlaySkillLifebuoyActionState(PlayManager manager, WarTeam warTeam, Integer index) {
        this.manager = manager;
        this.warTeam = warTeam;
        this.index = index;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(manager.getContainer(), warTeam, Skill.LIFEBUOY));
        commands.add(new PlaySkillEnableTeamMemberCommand(manager, warTeam, index));
    }
}
