package com.ww.game.play.state.challenge;

import com.ww.game.play.PlayManager;
import com.ww.game.play.state.war.PlayChangingTaskState;
import com.ww.model.container.rival.challenge.ChallengeTeam;

import static com.ww.helper.TeamHelper.BOT_PROFILE_ID;

public class PlayChallengeChangingTaskState extends PlayChangingTaskState {
    public PlayChallengeChangingTaskState(PlayManager manager) {
        super(manager);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        ChallengeTeam team = (ChallengeTeam) getContainer().getTeams().team(BOT_PROFILE_ID);
        if (!team.getActiveTeamMember().isPresent()) {
            ((ChallengeTeam) getContainer().getTeams().opponent(BOT_PROFILE_ID)).increaseScore();
        }
    }
}
