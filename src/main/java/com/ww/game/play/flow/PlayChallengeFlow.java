package com.ww.game.play.flow;

import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.game.play.PlayManager;
import com.ww.game.play.state.PlayAnsweredState;
import com.ww.game.play.state.PlayIntroState;
import com.ww.game.play.state.PlayState;
import com.ww.game.play.state.challenge.PlayChallengeAnsweredState;
import com.ww.game.play.state.challenge.PlayChallengeChoosingWhoAnswerState;

public class PlayChallengeFlow extends PlayWarFlow {

    public PlayChallengeFlow(PlayManager manager, RivalInterval interval) {
        super(manager, interval);
    }

    @Override
    protected synchronized PlayState createChoosingWhoAnswerState() {
        return new PlayChallengeChoosingWhoAnswerState(getContainer(), ((WarInterval) interval).getChoosingWhoAnswerInterval());
    }

    @Override
    protected synchronized PlayAnsweredState createAnsweredState(Long profileId, Long answerId) {
        return new PlayChallengeAnsweredState(getContainer(), profileId, answerId);
    }
}
