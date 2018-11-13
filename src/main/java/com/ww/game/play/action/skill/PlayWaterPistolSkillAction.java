package com.ww.game.play.action.skill;

import com.ww.game.play.PlayManager;
import com.ww.game.play.action.PlayAction;
import com.ww.game.play.flow.PlayWarFlow;
import com.ww.game.play.state.skill.ninja.PlaySkillNinjaActionState;
import com.ww.game.play.state.skill.waterpistol.PlaySkillWaterPistolActionState;
import com.ww.model.constant.Skill;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

public class PlayWaterPistolSkillAction extends PlayAction {

    public PlayWaterPistolSkillAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!getFlow().isStatusEquals(RivalStatus.ANSWERING)) {
            return;
        }
        WarTeam warTeam = (WarTeam) getContainer().getTeams().team(profileId);
        WarTeam warOpponentTeam = (WarTeam) getContainer().getTeams().opponent(warTeam);
        if (!warTeam.getTeamSkills().canUse(Skill.WATER_PISTOL)
                || !warOpponentTeam.getActiveTeamMember().isWisie()
                || !warTeam.getActiveTeamMember().isWisie()
                || !warOpponentTeam.getActiveTeamMember().isPresent()) {
            return;
        }
        ((PlayWarFlow) getFlow()).skillAction(new PlaySkillWaterPistolActionState(manager, warTeam, warOpponentTeam));
    }
}
