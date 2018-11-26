package com.ww.game.play.state.skill.coverall;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.game.play.command.skill.flow.PlaySkillStartCoverallFlowCommand;
import com.ww.game.play.command.skill.flow.PlaySkillStartPizzaFlowCommand;
import com.ww.game.play.state.skill.PlaySkillActionState;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillCoverallActionState extends PlaySkillActionState {
    protected WarTeam warTeam;
    protected WarTeam warOpponentTeam;

    public PlaySkillCoverallActionState(PlayManager manager, WarTeam warTeam, WarTeam warOpponentTeam) {
        super(manager);
        this.warTeam = warTeam;
        this.warOpponentTeam = warOpponentTeam;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(manager.getContainer(), warTeam, Skill.COVERALL));
        commands.add(new PlaySkillStartCoverallFlowCommand(flowContainer, warTeam.getProfileId(), warOpponentTeam.getProfileId()));
    }
}
