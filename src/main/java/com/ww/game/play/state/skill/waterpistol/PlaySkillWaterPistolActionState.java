package com.ww.game.play.state.skill.waterpistol;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.game.play.command.skill.flow.PlaySkillStartNinjaFlowCommand;
import com.ww.game.play.command.skill.flow.PlaySkillStartWaterPistolFlowCommand;
import com.ww.game.play.state.skill.PlaySkillActionState;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.war.WarTeam;

public class PlaySkillWaterPistolActionState extends PlaySkillActionState {
    protected WarTeam warTeam;
    protected WarTeam warOpponentTeam;

    public PlaySkillWaterPistolActionState(PlayManager manager, WarTeam warTeam, WarTeam warOpponentTeam) {
        super(manager);
        this.warTeam = warTeam;
        this.warOpponentTeam = warOpponentTeam;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(manager.getContainer(), warTeam, Skill.WATER_PISTOL));
        commands.add(new PlaySkillStartWaterPistolFlowCommand(flowContainer, warTeam.getProfileId(), warOpponentTeam.getProfileId()));
    }
}
