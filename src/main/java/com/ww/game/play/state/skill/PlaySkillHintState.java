package com.ww.game.play.state.skill;

import com.ww.game.GameState;
import com.ww.game.play.command.skill.PlaySkillStartHintFlowCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillHintState extends GameState {
    protected PlayContainer container;
    protected WarTeam warTeam;
    protected Long answerId;

    public PlaySkillHintState(PlayContainer container, WarTeam warTeam, Long answerId) {
        this.container = container;
        this.warTeam = warTeam;
        this.answerId = answerId;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(container, warTeam, Skill.HINT));
        commands.add(new PlaySkillStartHintFlowCommand(container, warTeam, answerId));
    }
}
