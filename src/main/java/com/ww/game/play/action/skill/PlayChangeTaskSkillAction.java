package com.ww.game.play.action.skill;

import com.ww.game.play.PlayManager;
import com.ww.game.play.action.PlayAction;
import com.ww.game.play.flow.PlayWarFlow;
import com.ww.game.play.state.skill.changetask.PlaySkillChangeTaskActionState;
import com.ww.model.constant.Skill;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

public class PlayChangeTaskSkillAction extends PlayAction {

    public PlayChangeTaskSkillAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!getFlow().isStatusEquals(RivalStatus.ANSWERING)) {
            return;
        }
        WarTeam warTeam = (WarTeam) getContainer().getTeams().team(profileId);
        if (!warTeam.getTeamSkills().canUse(Skill.CHANGE_TASK)
                || !warTeam.getActiveTeamMember().isWisie()
                || !warTeam.getActiveTeamMember().isPresent()) {
            return;
        }
        try {
            ((PlayWarFlow) getFlow()).skillAction(new PlaySkillChangeTaskActionState(manager, warTeam));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
