package com.ww.game.play.state.skill.ninja;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.game.play.command.skill.flow.PlaySkillStartNinjaFlowCommand;
import com.ww.game.play.state.skill.PlaySkillActionState;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillNinjaActionState extends PlaySkillActionState {
    protected WarTeam warTeam;
    protected WarTeam warOpponentTeam;

    public PlaySkillNinjaActionState(PlayManager manager, WarTeam warTeam, WarTeam warOpponentTeam) {
        super(manager);
        this.warTeam = warTeam;
        this.warOpponentTeam = warOpponentTeam;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(manager.getContainer(), warTeam, Skill.NINJA));
        commands.add(new PlaySkillStartNinjaFlowCommand(flowContainer, warTeam.getProfileId(), warOpponentTeam.getProfileId()));
    }
}
