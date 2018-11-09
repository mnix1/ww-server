package com.ww.game.play.state.skill.ghost;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.flow.PlaySkillStartGhostFlowCommand;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillGhostActionState extends GameState {
    protected PlayManager manager;
    protected WarTeam warTeam;
    protected WarTeam warOpponentTeam;

    public PlaySkillGhostActionState(PlayManager manager, WarTeam warTeam, WarTeam warOpponentTeam) {
        this.manager = manager;
        this.warTeam = warTeam;
        this.warOpponentTeam = warOpponentTeam;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(manager.getContainer(), warTeam, Skill.GHOST));
        commands.add(new PlaySkillStartGhostFlowCommand(manager, warTeam, warOpponentTeam));
    }
}
