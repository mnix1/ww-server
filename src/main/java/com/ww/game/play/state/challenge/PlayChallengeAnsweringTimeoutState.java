package com.ww.game.play.state.challenge;

import com.ww.game.play.PlayManager;
import com.ww.game.play.state.war.PlayWarAnsweringTimeoutState;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.challenge.ChallengeTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayChallengeModelFiller.fillModelNewScore;
import static com.ww.helper.TeamHelper.BOT_PROFILE_ID;

public class PlayChallengeAnsweringTimeoutState extends PlayWarAnsweringTimeoutState {

    public PlayChallengeAnsweringTimeoutState(PlayManager manager) {
        super(manager);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        ((ChallengeTeam) getContainer().getTeams().opponent(BOT_PROFILE_ID)).increaseScore();
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNewScore(model, (ChallengeTeam) team);
        return model;
    }
}
