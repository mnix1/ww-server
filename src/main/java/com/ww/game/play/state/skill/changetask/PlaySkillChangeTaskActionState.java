package com.ww.game.play.state.skill.changetask;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.game.play.command.skill.flow.PlaySkillStartChangeTaskFlowCommand;
import com.ww.game.play.command.skill.flow.PlaySkillStartHintFlowCommand;
import com.ww.game.play.state.skill.PlaySkillActionState;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillChangeTaskActionState extends PlaySkillActionState {
    protected WarTeam warTeam;

    public PlaySkillChangeTaskActionState(PlayManager manager, WarTeam warTeam) {
        super(manager);
        this.warTeam = warTeam;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(manager.getContainer(), warTeam, Skill.CHANGE_TASK));
        commands.add(new PlaySkillStartChangeTaskFlowCommand(manager, warTeam));
    }
}
