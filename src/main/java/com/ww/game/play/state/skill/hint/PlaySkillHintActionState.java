package com.ww.game.play.state.skill.hint;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.game.play.command.skill.flow.PlaySkillStartHintFlowCommand;
import com.ww.game.play.state.skill.PlaySkillActionState;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillHintActionState extends PlaySkillActionState {
    protected WarTeam warTeam;
    protected Long answerId;

    public PlaySkillHintActionState(PlayManager manager, WarTeam warTeam, Long answerId) {
        super(manager);
        this.warTeam = warTeam;
        this.answerId = answerId;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(manager.getContainer(), warTeam, Skill.HINT));
        commands.add(new PlaySkillStartHintFlowCommand(manager, warTeam, answerId));
    }
}
