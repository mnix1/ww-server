package com.ww.manager.rival.challenge.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarStateAnswered;
import com.ww.model.container.rival.challenge.ChallengeTeam;

import java.util.Map;

public class ChallengeStateAnswered extends WarStateAnswered {

    public ChallengeStateAnswered(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager, profileId, content);
    }

    protected void updateTeamPresent() {
        ChallengeTeam team = (ChallengeTeam) manager.getTeam(profileId);
        if (manager.getModel().isOpponent()) {
            if (isAnswerCorrect) {
                team.setScore(team.getScore() + 1);
                team = (ChallengeTeam) manager.getModel().getTeams().opponent(team.getProfileId());
            }
            team.setActiveTeamMemberPresentToFalse();
        } else if (!isAnswerCorrect) {
            team.setActiveTeamMemberPresentToFalse();
        }
    }

}
