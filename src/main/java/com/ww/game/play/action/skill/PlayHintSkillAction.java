package com.ww.game.play.action.skill;

import com.ww.game.play.PlayManager;
import com.ww.game.play.action.PlayAction;
import com.ww.game.play.flow.PlayWarFlow;
import com.ww.game.play.state.skill.hint.PlaySkillHintActionState;
import com.ww.model.constant.Skill;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

public class PlayHintSkillAction extends PlayAction {

    public PlayHintSkillAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!getFlow().isStatusEquals(RivalStatus.ANSWERING)
                || !content.containsKey("answerId")) {
            return;
        }
        WarTeam warTeam = (WarTeam) getContainer().getTeams().team(profileId);
        if (!warTeam.getTeamSkills().canUse(Skill.HINT)
                || !warTeam.getActiveTeamMember().isWisie()) {
            return;
        }
        try {
            Long answerId = ((Integer) content.get("answerId")).longValue();
            ((PlayWarFlow) getFlow()).skillAction(new PlaySkillHintActionState(manager, warTeam, answerId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
