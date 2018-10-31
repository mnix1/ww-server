package com.ww.play.state.challenge;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.challenge.ChallengeTeam;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.war.PlayWarAnsweredState;

import java.util.Map;

import static com.ww.helper.TeamHelper.isBotProfile;
import static com.ww.play.modelfiller.PlayChallengeModelFiller.fillModelNewScore;

public class PlayChallengeAnsweredState extends PlayWarAnsweredState {

    public PlayChallengeAnsweredState(PlayContainer container, Long profileId, Long answerId) {
        super(container, profileId, answerId);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        if (!isBotProfile(profileId) && isCorrect) {
            ((ChallengeTeam) container.getTeams().team(profileId)).increaseScore();
        }
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNewScore(model, (ChallengeTeam) team);
        return model;
    }
}
