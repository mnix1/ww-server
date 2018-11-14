package com.ww.game.play.state.skill.pizza;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.game.play.command.skill.flow.PlaySkillStartGhostFlowCommand;
import com.ww.game.play.command.skill.flow.PlaySkillStartPizzaFlowCommand;
import com.ww.game.play.state.skill.PlaySkillActionState;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillPizzaActionState extends PlaySkillActionState {
    protected WarTeam warTeam;
    protected WarTeam warOpponentTeam;

    public PlaySkillPizzaActionState(PlayManager manager, WarTeam warTeam, WarTeam warOpponentTeam) {
        super(manager);
        this.warTeam = warTeam;
        this.warOpponentTeam = warOpponentTeam;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(manager.getContainer(), warTeam, Skill.PIZZA));
        commands.add(new PlaySkillStartPizzaFlowCommand(manager, warTeam, warOpponentTeam));
    }
}
