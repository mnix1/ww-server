package com.ww.game.play.state.challenge;

import com.ww.game.play.PlayManager;
import com.ww.game.play.state.war.PlayWarAnsweredState;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.challenge.ChallengeTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayChallengeModelFiller.fillModelNewScore;
import static com.ww.helper.TeamHelper.isBotProfile;

public class PlayChallengeAnsweredState extends PlayWarAnsweredState {

    public PlayChallengeAnsweredState(PlayManager manager, Long profileId, Long answerId) {
        super(manager, profileId, answerId);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        if (!isBotProfile(profileId) && isCorrect) {
            ((ChallengeTeam) getContainer().getTeams().team(profileId)).increaseScore();
        }
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNewScore(model, (ChallengeTeam) team);
        return model;
    }
}
