package com.ww.play.command.challenge;

import com.ww.helper.TeamHelper;
import com.ww.model.container.rival.challenge.ChallengeTeam;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import com.ww.play.command.PlayCommand;
import com.ww.play.container.PlayChallengeContainer;
import com.ww.play.container.PlayContainer;

import java.util.Collections;

import static com.ww.helper.TeamHelper.BOT_PROFILE_ID;

public class PlayChallengeSwapBotTeamCommand extends PlayCommand {

    public PlayChallengeSwapBotTeamCommand(PlayContainer container) {
        super(container);
    }

    @Override
    public void execute() {
        ChallengeTeam challengeTeam = (ChallengeTeam) container.getTeams().team(BOT_PROFILE_ID);
        if (!challengeTeam.isAnyPresentMember()) {
            ChallengePhase challengePhase = ((PlayChallengeContainer) container).currentPhase();
            challengeTeam.setTeamMembers(TeamHelper.prepareTeamMembers(Collections.singletonList(challengePhase.getPhaseWisie())));
        }
    }
}
