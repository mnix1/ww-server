package com.ww.manager.rival.challenge.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarState;
import com.ww.manager.rival.war.state.WarStateAnswered;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.challenge.ChallengeTeam;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ChallengeStateAnswered extends WarStateAnswered {

    public ChallengeStateAnswered(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager, profileId, content);
    }

    protected void updateTeamPresent() {
        ChallengeTeam team = (ChallengeTeam) manager.getTeam(profileId);
        if (manager.getModel().isOpponent()) {
            if (isAnswerCorrect) {
                team.setScore(team.getScore() + 1);
                team = (ChallengeTeam) manager.getModel().getTeams().opponentTeam(team.getProfileId());
            }
            team.setActiveTeamMemberPresentToFalse();
        } else if (!isAnswerCorrect) {
            team.setActiveTeamMemberPresentToFalse();
        }
    }

}
