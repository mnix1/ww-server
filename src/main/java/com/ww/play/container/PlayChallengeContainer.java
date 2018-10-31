package com.ww.play.container;

import com.ww.model.container.rival.*;
import com.ww.model.container.rival.init.RivalChallengeInit;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;

import static com.ww.helper.TeamHelper.isBotProfile;

public class PlayChallengeContainer extends PlayCampaignContainer {
    public PlayChallengeContainer(RivalTwoInit init, RivalTeams teams, RivalTasks tasks, RivalTimeouts timeouts, RivalDecisions decisions, RivalResult result) {
        super(init, teams, tasks, timeouts, decisions, result);
    }

    @Override
    public boolean isEnd() {
        for (RivalTeam team : teams.getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            if (!isBotProfile(team.getProfile()) && !warTeam.isAnyPresentMember()) {
                return true;
            }
        }
        return false;
    }

    public ChallengePhase currentPhase() {
        RivalChallengeInit challengeInit = (RivalChallengeInit) init;
        return challengeInit.getChallengePhases().get(tasks.taskIndex());
    }

}
