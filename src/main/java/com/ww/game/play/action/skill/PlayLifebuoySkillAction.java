package com.ww.game.play.action.skill;

import com.ww.game.play.PlayManager;
import com.ww.game.play.action.PlayAction;
import com.ww.game.play.flow.PlayWarFlow;
import com.ww.game.play.state.skill.lifebuoy.PlaySkillLifebuoyActionState;
import com.ww.model.constant.Skill;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;
import java.util.Optional;

public class PlayLifebuoySkillAction extends PlayAction {

    public PlayLifebuoySkillAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if ((!getFlow().isStatusEquals(RivalStatus.CHOOSING_WHO_ANSWER)
                && !getFlow().isStatusEquals(RivalStatus.CHOSEN_WHO_ANSWER))
                || !content.containsKey("index")) {
            return;
        }
        WarTeam warTeam = (WarTeam) getContainer().getTeams().team(profileId);
        if (!warTeam.getTeamSkills().canUse(Skill.LIFEBUOY)) {
            return;
        }
        try {
            Integer index = ((Integer) content.get("index"));
            Optional<TeamMember> optionalTeamMember = warTeam.getTeamMembers().stream().filter(teamMember -> teamMember.getIndex() == index).findFirst();
            if (!optionalTeamMember.isPresent()) {
                return;
            }
            TeamMember teamMember = optionalTeamMember.get();
            if (!teamMember.isWisie() || teamMember.isPresent()) {
                return;
            }
            ((PlayWarFlow) getFlow()).skillAction(new PlaySkillLifebuoyActionState(manager, warTeam, index));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
